package cat.yoink.xanax.internal.module.impl.toggleable.combat;

import cat.yoink.xanax.internal.module.ModuleCategory;
import cat.yoink.xanax.internal.module.main.ModuleData;
import cat.yoink.xanax.internal.module.state.StateModule;
import cat.yoink.xanax.internal.setting.types.NumberSetting;
import cat.yoink.xanax.internal.util.InventoryUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

/**
 * @author yoink
 */
@ModuleData(name = "AutoCreeper", category = ModuleCategory.COMBAT, description = "wtf is this please help")
public final class AutoCreeper extends StateModule
{
    private final NumberSetting distance = addSetting(new NumberSetting("Distance", 4, 2, 7, 0.1));
    private final NumberSetting delay = addSetting(new NumberSetting("Delay", 3, 0, 20, 1));
    private int oldSlot;

    @Override
    public void onEnable()
    {
        oldSlot = -1;
        int slot = InventoryUtil.getHotbarSlot(Items.SPAWN_EGG);
        if (slot != -1)
        {
            oldSlot = mc.player.inventory.currentItem;
            mc.player.inventory.currentItem = slot;
        }
        else toggle();
    }

    @Override
    public void onDisable()
    {
        if (oldSlot != -1) mc.player.inventory.currentItem = oldSlot;
    }

    @SubscribeEvent
    public void onTickClientTick(TickEvent.ClientTickEvent event)
    {
        if (isSafe())
        {
            if (mc.player.ticksExisted % delay.getValue() + 1 != 1) return;
            for (EntityPlayer player : mc.world.playerEntities)
            {
                if (mc.player.getDistance(player) <= distance.getValue() && !player.equals(mc.player))
                {
                    BlockPos blockPos = new BlockPos(player.posX, player.posY - 1, player.posZ);
                    if (!mc.world.getBlockState(blockPos).getBlock().equals(Blocks.AIR))
                    {
                        if (!mc.player.getHeldItemMainhand().getItem().equals(Items.SPAWN_EGG))
                        {
                            int slot = InventoryUtil.getHotbarSlot(Items.SPAWN_EGG);
                            if (slot == -1)
                            {
                                toggle();
                                continue;
                            }
                            mc.player.inventory.currentItem = slot;
                        }
                        mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(blockPos, EnumFacing.UP, EnumHand.MAIN_HAND, blockPos.getX(), blockPos.getY(), blockPos.getZ()));
                        mc.player.swingArm(EnumHand.MAIN_HAND);
                    }
                }
            }
        }
    }
}
