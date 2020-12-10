package cat.yoink.xanax.internal.module.impl.toggleable.world;

import cat.yoink.xanax.internal.event.impl.DamageBlockEvent;
import cat.yoink.xanax.internal.module.ModuleCategory;
import cat.yoink.xanax.internal.module.main.ModuleData;
import cat.yoink.xanax.internal.module.state.StateModule;
import cat.yoink.xanax.internal.setting.types.ListSetting;
import cat.yoink.xanax.internal.setting.types.NumberSetting;
import cat.yoink.xanax.internal.setting.types.StateSetting;
import cat.yoink.xanax.internal.util.InventoryUtil;
import cat.yoink.xanax.internal.util.RenderUtil;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;

/**
 * @author yoink
 */
@ModuleData(name = "PacketMine", category = ModuleCategory.WORLD, description = "Mine blocks with packets")
public final class PacketMine extends StateModule
{
    private final ListSetting render = addSetting(new ListSetting("Render", "Specific", "Off", "Full", "Specific"));
    private final NumberSetting red = addSetting(new NumberSetting("Red", v -> !render.getValue().equals("Off"), 200, 0, 255, 1));
    private final NumberSetting green = addSetting(new NumberSetting("Green", v -> !render.getValue().equals("Off"), 10, 0, 255, 1));
    private final StateSetting box = addSetting(new StateSetting("Box", v -> !render.getValue().equals("Off"), true));
    private final NumberSetting blue = addSetting(new NumberSetting("Blue", v -> !render.getValue().equals("Off"), 10, 0, 255, 1));
    private final StateSetting outline = addSetting(new StateSetting("Outline", v -> !render.getValue().equals("Off"), true));
    private final NumberSetting alpha = addSetting(new NumberSetting("Alpha", v -> !render.getValue().equals("Off"), 100, 0, 255, 1));
    private final StateSetting change = addSetting(new StateSetting("Change", v -> !render.getValue().equals("Off"), false));
    private final StateSetting noBreak = addSetting(new StateSetting("NoBreak", false));
    private final StateSetting swing = addSetting(new StateSetting("Swing", false));
    private final StateSetting silent = addSetting(new StateSetting("Silent", false));
    private final NumberSetting time = addSetting(new NumberSetting("Time", v -> !render.getValue().equals("Off"), 300, 100, 1000, 10));
    private BlockPos breakBlock;
    private int miningTicks;

    @SubscribeEvent
    public void onDamageBlock(DamageBlockEvent event)
    {
        if (isSafe())
        {
            if (swing.getValue()) mc.player.swingArm(EnumHand.MAIN_HAND);
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, event.getPos(), event.getFace()));
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, event.getPos(), event.getFace()));

            if (render.getValue().equals("Full") || render.getValue().equals("Specific")) breakBlock = event.getPos();
            if (noBreak.getValue()) event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event)
    {
        if (isSafe())
        {
            if (silent.getValue() && breakBlock != null && !(mc.player.inventory.getItemStack().getItem() instanceof ItemPickaxe))
            {
                int slot = InventoryUtil.getHotbarSlot(Items.DIAMOND_PICKAXE);
                if (slot != -1) mc.player.connection.sendPacket(new CPacketHeldItemChange(slot));
            }
            if (breakBlock != null) miningTicks++;
            if (breakBlock != null && miningTicks > time.getValue())
            {
                miningTicks = 0;
                breakBlock = null;

                System.out.println(mc.player.inventory.currentItem);
                if (silent.getValue()) mc.player.connection.sendPacket(new CPacketHeldItemChange(mc.player.inventory.currentItem));
            }
        }
    }

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event)
    {
        if (isSafe())
        {
            if (breakBlock != null && mc.world.getBlockState(breakBlock).getBlock() == Blocks.AIR)
            {
                breakBlock = null;
                miningTicks = 0;
                if (silent.getValue()) mc.player.connection.sendPacket(new CPacketHeldItemChange(mc.player.inventory.currentItem));
            }
            else if (breakBlock != null && miningTicks != 0)
            {
                Color c;
                if (change.getValue())
                {
                    if (miningTicks < 50) c = new Color(200, 10, 10, 150);
                    else c = new Color(10, 200, 10, 150);
                }
                else
                {
                    c = new Color(red.getValue().intValue() / 255f, green.getValue().intValue() / 255f, blue.getValue().intValue() / 255f, alpha.getValue().intValue() / 255f);
                }

                if (render.getValue().equals("Specific"))
                {
                    AxisAlignedBB bb = RenderUtil.convertBox(mc.world.getBlockState(breakBlock).getBoundingBox(mc.world, breakBlock).offset(breakBlock));
                    RenderUtil.drawBox(bb, c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha(), box.getValue(), outline.getValue());
                }
                else
                {
                    RenderUtil.drawBox(breakBlock, c, box.getValue(), outline.getValue());
                }
            }
        }
    }
}
