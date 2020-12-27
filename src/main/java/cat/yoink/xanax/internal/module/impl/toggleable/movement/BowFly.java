package cat.yoink.xanax.internal.module.impl.toggleable.movement;

import cat.yoink.xanax.internal.event.impl.PacketEvent;
import cat.yoink.xanax.internal.module.ModuleCategory;
import cat.yoink.xanax.internal.module.main.ModuleData;
import cat.yoink.xanax.internal.module.state.StateModule;
import cat.yoink.xanax.internal.util.WorldUtil;
import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Mouse;

/**
 * @author yoink
 */
@ModuleData(
        name = "BowFly",
        aliases = {"BowFly", "BowFlight"},
        category = ModuleCategory.MOVEMENT,
        description = "Allows you to fly upwards using a bow",
        noSave = true
)
public final class BowFly extends StateModule
{
    private float prev;

    @SubscribeEvent
    public void onTickClientTick(TickEvent.ClientTickEvent event)
    {
        if (isSafe() && Mouse.isButtonDown(1))
        {
            if (prev > mc.player.getHealth() + mc.player.getAbsorptionAmount())
            {
                mc.player.motionX = 0;
                mc.player.motionY = 0.6;
                mc.player.motionZ = 0;
            }
            if (mc.player.motionY < -0.08 &&  mc.player.inventory.getCurrentItem().getItem() instanceof ItemBow)
            {
                WorldUtil.setTPS(3);
                if (mc.player.isHandActive() && mc.player.getItemInUseMaxCount() > 1)
                {
                    mc.player.connection.sendPacket(new CPacketPlayer.Rotation(mc.player.rotationYaw, -90, mc.player.onGround));
                    mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, mc.player.getHorizontalFacing()));
                    mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(mc.player.getActiveHand()));
                    mc.player.stopActiveHand();
                }
            }
            else WorldUtil.setTPS(20);
            prev = mc.player.getHealth() + mc.player.getAbsorptionAmount();
        }
        else WorldUtil.setTPS(20);
    }

    @SubscribeEvent
    public void onPacket(PacketEvent event)
    {
        if (isSafe() && event.getType().equals(PacketEvent.Type.INCOMING))
        {
            if (event.getPacket() instanceof SPacketPlayerPosLook)
            {
                SPacketPlayerPosLook packet = (SPacketPlayerPosLook) event.getPacket();
                packet.yaw = mc.player.rotationYaw;
                packet.pitch = mc.player.rotationPitch;
            }
        }
    }

    @SubscribeEvent
    public void onDamage(LivingDamageEvent event)
    {
        if (isSafe() && event.getEntity().equals(mc.player))
        {
            mc.player.motionX = 0;
            mc.player.motionY = 0.5;
            mc.player.motionZ = 0;
//            mc.player.posY = mc.player.posY + 2;
        }
    }

    @Override
    public void onDisable()
    {
        WorldUtil.setTPS(20);
    }
}
