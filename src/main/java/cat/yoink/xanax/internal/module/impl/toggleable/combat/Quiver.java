package cat.yoink.xanax.internal.module.impl.toggleable.combat;

import cat.yoink.xanax.internal.module.ModuleCategory;
import cat.yoink.xanax.internal.module.main.ModuleData;
import cat.yoink.xanax.internal.module.state.StateModule;
import cat.yoink.xanax.internal.setting.types.NumberSetting;
import cat.yoink.xanax.internal.setting.types.StateSetting;
import cat.yoink.xanax.internal.util.InventoryUtil;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.List;

/**
 * @author yoink
 */
@ModuleData(name = "Quiver", category = ModuleCategory.COMBAT)
public final class Quiver extends StateModule
{
    private final StateSetting release = addSetting(new StateSetting("Release", true));
    private final StateSetting arrange = addSetting(new StateSetting("Arrange", true));
    private final NumberSetting shootDelay = addSetting(new NumberSetting("Delay", v -> release.getValue(), 3, 2, 10, 1));

    @SubscribeEvent
    public void onTickClientTick(TickEvent.ClientTickEvent event)
    {
        if (isSafe())
        {
            if (release.getValue() && mc.player.inventory.getCurrentItem().getItem() instanceof ItemBow && mc.player.isHandActive() && mc.player.getItemInUseMaxCount() > shootDelay.getValue())
            {
                mc.player.connection.sendPacket(new CPacketPlayer.Rotation(mc.player.rotationYaw, -90, mc.player.onGround));
                mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, mc.player.getHorizontalFacing()));
                mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(mc.player.getActiveHand()));
                mc.player.stopActiveHand();
            }

            if (arrange.getValue())
            {
                boolean hasSpeed = mc.player.getActivePotionEffect(Potion.getPotionById(1)) != null;
                boolean hasStrength = mc.player.getActivePotionEffect(Potion.getPotionById(5)) != null;

                List<Integer> arrowSlots = InventoryUtil.getInventorySlots(Items.TIPPED_ARROW);

                int speedSlot = -1;
                int strengthSlot = -1;
                for (Integer slot : arrowSlots)
                {
                    if (PotionUtils.getPotionFromItem(mc.player.inventory.getStackInSlot(slot)).getRegistryName().getPath().contains("swiftness")) speedSlot = slot;
                    else if (PotionUtils.getPotionFromItem(mc.player.inventory.getStackInSlot(slot)).getRegistryName().getPath().contains("strength")) strengthSlot = slot;
                }

                if (strengthSlot == -1 || speedSlot == -1 || hasSpeed && hasStrength) return;
                if ((hasSpeed && speedSlot < strengthSlot) || (hasStrength && strengthSlot < speedSlot)) InventoryUtil.swapSlots(strengthSlot, speedSlot);
            }
        }
    }
}
