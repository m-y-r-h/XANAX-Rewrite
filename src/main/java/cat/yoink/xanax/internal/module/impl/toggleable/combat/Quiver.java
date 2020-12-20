package cat.yoink.xanax.internal.module.impl.toggleable.combat;

import cat.yoink.xanax.internal.module.ModuleCategory;
import cat.yoink.xanax.internal.module.main.ModuleData;
import cat.yoink.xanax.internal.module.state.StateModule;
import cat.yoink.xanax.internal.setting.annotation.Setting;
import cat.yoink.xanax.internal.setting.annotation.setting.Boolean;
import cat.yoink.xanax.internal.setting.annotation.setting.Number;
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
import java.util.Objects;

/**
 * @author yoink
 */
@ModuleData(name = "Quiver", category = ModuleCategory.COMBAT)
public final class Quiver extends StateModule
{
    @Setting(name = "Release") @Boolean(true) public boolean release;
    @Setting(name = "Arrange") @Boolean(true) public boolean arrange;
    @Setting(name = "Delay") @Number(value = 3, min = 2) public double delay;

    @SubscribeEvent
    public void onTickClientTick(TickEvent.ClientTickEvent event)
    {
        if (isSafe())
        {
            boolean hasSpeed = mc.player.getActivePotionEffect(Objects.requireNonNull(Potion.getPotionById(1))) != null;
            boolean hasStrength = mc.player.getActivePotionEffect(Objects.requireNonNull(Potion.getPotionById(5))) != null;

            if (release && (!hasSpeed || !hasStrength)  && mc.player.inventory.getCurrentItem().getItem() instanceof ItemBow && mc.player.isHandActive() && mc.player.getItemInUseMaxCount() > delay)
            {
                mc.player.connection.sendPacket(new CPacketPlayer.Rotation(mc.player.rotationYaw, -90, mc.player.onGround));
                mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, mc.player.getHorizontalFacing()));
                mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(mc.player.getActiveHand()));
                mc.player.stopActiveHand();
            }

            if (arrange)
            {
                List<Integer> arrowSlots = InventoryUtil.getInventorySlots(Items.TIPPED_ARROW);

                int speedSlot = -1;
                int strengthSlot = -1;
                for (Integer slot : arrowSlots)
                {
                    if (Objects.requireNonNull(PotionUtils.getPotionFromItem(mc.player.inventory.getStackInSlot(slot)).getRegistryName()).getPath().contains("swiftness")) speedSlot = slot;
                    else if (Objects.requireNonNull(PotionUtils.getPotionFromItem(mc.player.inventory.getStackInSlot(slot)).getRegistryName()).getPath().contains("strength")) strengthSlot = slot;
                }

                if (strengthSlot == -1 || speedSlot == -1 || hasSpeed && hasStrength) return;
                if ((hasSpeed && speedSlot < strengthSlot) || (hasStrength && strengthSlot < speedSlot)) InventoryUtil.swapSlots(strengthSlot, speedSlot);
            }
        }
    }
}
