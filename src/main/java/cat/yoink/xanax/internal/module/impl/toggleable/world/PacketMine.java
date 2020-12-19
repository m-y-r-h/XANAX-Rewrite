package cat.yoink.xanax.internal.module.impl.toggleable.world;

import cat.yoink.xanax.internal.event.impl.DamageBlockEvent;
import cat.yoink.xanax.internal.module.ModuleCategory;
import cat.yoink.xanax.internal.module.main.ModuleData;
import cat.yoink.xanax.internal.module.state.StateModule;
import cat.yoink.xanax.internal.setting.annotation.Name;
import cat.yoink.xanax.internal.setting.annotation.setting.Boolean;
import cat.yoink.xanax.internal.setting.annotation.setting.Color;
import cat.yoink.xanax.internal.setting.annotation.setting.List;
import cat.yoink.xanax.internal.setting.annotation.setting.Number;
import cat.yoink.xanax.internal.util.InventoryUtil;
import cat.yoink.xanax.internal.util.RenderUtil;
import cat.yoink.xanax.internal.util.Timer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

/**
 * @author yoink
 */
@ModuleData(name = "PacketMine", category = ModuleCategory.WORLD, description = "Mine blocks with packets")
public final class PacketMine extends StateModule
{

    @Name("Render") @List({"Specific", "Full", "Off"}) public String render;
    @Name("Color") @Color(-16776961) public java.awt.Color color;
    @Name("Box") @Boolean(true) public boolean box;
    @Name("Alpha") @Number(value = 100, maximum = 255) public double alpha;
    @Name("Outline") @Boolean(true) public boolean outline;
    @Name("Change") @Boolean(false) public boolean change;
    @Name("NoBreak") @Boolean(false) public boolean noBreak;
    @Name("Swing") @Boolean(true) public boolean swing;
    @Name("Silent") @Boolean(false) public boolean silent;
    @Name("Time") @Number(value = 300, minimum = 100, maximum = 1000, increment = 10) public double time;
    @Name("CancelClick") @Boolean(true) public boolean cancel;
    private BlockPos breakBlock;
    private final Timer timer = new Timer();

    @SubscribeEvent
    public void onDamageBlock(DamageBlockEvent event)
    {
        if (isSafe())
        {
            if (swing) mc.player.swingArm(EnumHand.MAIN_HAND);
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, event.getPos(), event.getFace()));
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, event.getPos(), event.getFace()));

            if (render.equals("Full") || render.equals("Specific")) breakBlock = event.getPos();
            if (noBreak) event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event)
    {
        if (isSafe())
        {
            if (silent && breakBlock != null && !(mc.player.inventory.getItemStack().getItem() instanceof ItemPickaxe))
            {
                int slot = InventoryUtil.getHotbarSlot(Items.DIAMOND_PICKAXE);
                if (slot != -1) mc.player.connection.sendPacket(new CPacketHeldItemChange(slot));
            }
            if (breakBlock != null) timer.tick();
            if (breakBlock != null && timer.hasPassed((int) time))
            {
                timer.reset();
                breakBlock = null;

                if (silent) mc.player.connection.sendPacket(new CPacketHeldItemChange(mc.player.inventory.currentItem));
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
                timer.reset();
                if (silent) mc.player.connection.sendPacket(new CPacketHeldItemChange(mc.player.inventory.currentItem));
            }
            else if (breakBlock != null && timer.getTicks() != 0)
            {
                java.awt.Color c;
                if (change) c = !timer.hasPassed(50) ? new java.awt.Color(200, 10, 10, 150) : new java.awt.Color(10, 200, 10, 150);
                else c = new java.awt.Color(color.getRed(), color.getGreen(), color.getBlue(), (int) alpha);

                if (render.equals("Specific")) RenderUtil.drawBox(RenderUtil.convertBox(mc.world.getBlockState(breakBlock).getBoundingBox(mc.world, breakBlock).offset(breakBlock)), c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha(), box, outline);
                else RenderUtil.drawBox(breakBlock, c, box, outline);
            }
        }
    }

    @SubscribeEvent
    public void onInputMouseInput(InputEvent.MouseInputEvent event)
    {
        if (isSafe() && cancel)
        {
            mc.player.connection.sendPacket(new CPacketHeldItemChange(mc.player.inventory.currentItem));
        }
    }
}
