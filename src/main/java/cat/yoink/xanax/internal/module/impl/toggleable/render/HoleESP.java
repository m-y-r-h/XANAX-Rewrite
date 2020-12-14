package cat.yoink.xanax.internal.module.impl.toggleable.render;

import cat.yoink.xanax.internal.module.ModuleCategory;
import cat.yoink.xanax.internal.module.main.ModuleData;
import cat.yoink.xanax.internal.module.state.StateModule;
import cat.yoink.xanax.internal.setting.types.NumberSetting;
import cat.yoink.xanax.internal.setting.types.StateSetting;
import cat.yoink.xanax.internal.util.RenderUtil;
import cat.yoink.xanax.internal.util.WorldUtil;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yoink
 */
@ModuleData(name = "HoleESP", category = ModuleCategory.RENDER, description = "Highlights nearby holes")
public final class HoleESP extends StateModule
{
    private final NumberSetting bedrockRed = addSetting(new NumberSetting("BedrockRed", 10, 0, 255, 1));
    private final NumberSetting obsidianRed = addSetting(new NumberSetting("ObsidianRed", 10, 0, 255, 1));
    private final NumberSetting bedrockGreen = addSetting(new NumberSetting("BedrockGreen", 10, 0, 255, 1));
    private final NumberSetting obsidianGreen = addSetting(new NumberSetting("ObsidianGreen", 10, 0, 255, 1));
    private final NumberSetting bedrockBlue = addSetting(new NumberSetting("BedrockBlue", 10, 0, 255, 1));
    private final NumberSetting obsidianBlue = addSetting(new NumberSetting("ObsidianBlue", 10, 0, 255, 1));
    private final NumberSetting bedrockAlpha = addSetting(new NumberSetting("BedrockAlpha", 150, 0, 255, 1));
    private final NumberSetting obsidianAlpha = addSetting(new NumberSetting("ObsidianAlpha", 150, 0, 255, 1));
    private final NumberSetting height = addSetting(new NumberSetting("Height", 0.1, -1, 1, 0.1));
    private final NumberSetting range = addSetting(new NumberSetting("Range", 8, 2, 20, 1));
    private final StateSetting box = addSetting(new StateSetting("Box", true));
    private final StateSetting outline = addSetting(new StateSetting("Outline", true));
    private final StateSetting wide = addSetting(new StateSetting("Wide", false));
    private final List<BlockPos> bedrockHoles = new ArrayList<>();
    private final List<BlockPos> obsidianHoles = new ArrayList<>();
    private final List<BlockPos> doubleBedrockHolesX = new ArrayList<>();
    private final List<BlockPos> doubleBedrockHolesZ = new ArrayList<>();
    private final List<BlockPos> doubleObsidianHolesX = new ArrayList<>();
    private final List<BlockPos> doubleObsidianHolesZ = new ArrayList<>();


    @SubscribeEvent
    public void onTickClientTick(TickEvent.ClientTickEvent event)
    {
        if (isSafe())
        {
            bedrockHoles.clear();
            obsidianHoles.clear();
            doubleBedrockHolesX.clear();
            doubleBedrockHolesZ.clear();
            doubleObsidianHolesX.clear();
            doubleObsidianHolesZ.clear();

            assert mc.renderViewEntity != null;
            Vec3i vec3i = new Vec3i(mc.renderViewEntity.posX, mc.renderViewEntity.posY, mc.renderViewEntity.posZ);

            for (int i = vec3i.getX() - range.getValue().intValue(); i < vec3i.getX() + range.getValue(); ++i)
            {
                for (int j = vec3i.getZ() - range.getValue().intValue(); j < vec3i.getZ() + range.getValue(); ++j)
                {
                    for (int k = vec3i.getY() + range.getValue().intValue(); k > vec3i.getY() - range.getValue(); --k)
                    {
                        BlockPos blockPos = new BlockPos(i, k, j);

                        if (WorldUtil.isBedrockHole(blockPos)) bedrockHoles.add(blockPos);
                        else if (WorldUtil.isHole(blockPos)) obsidianHoles.add(blockPos);
                        else if (wide.getValue())
                        {
                            if (WorldUtil.isDoubleBedrockHoleX(blockPos)) doubleBedrockHolesX.add(blockPos);
                            else if (WorldUtil.isDoubleBedrockHoleZ(blockPos)) doubleBedrockHolesZ.add(blockPos);
                            else if (WorldUtil.isDoubleHoleX(blockPos)) doubleObsidianHolesX.add(blockPos);
                            else if (WorldUtil.isDoubleHoleZ(blockPos)) doubleObsidianHolesZ.add(blockPos);
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event)
    {
        final int bedrockR = this.bedrockRed.getValue().intValue();
        final int bedrockG = this.bedrockGreen.getValue().intValue();
        final int bedrockB = this.bedrockBlue.getValue().intValue();
        final int bedrockA = this.bedrockAlpha.getValue().intValue();
        final int obsidianR = this.obsidianRed.getValue().intValue();
        final int obsidianG = this.obsidianGreen.getValue().intValue();
        final int obsidianB = this.obsidianBlue.getValue().intValue();
        final int obsidianA = this.obsidianAlpha.getValue().intValue();

        this.bedrockHoles.stream().map(hole -> new AxisAlignedBB(hole.getX() - mc.getRenderManager().viewerPosX, hole.getY() - mc.getRenderManager().viewerPosY, hole.getZ() - mc.getRenderManager().viewerPosZ, hole.getX() + 1 - mc.getRenderManager().viewerPosX, hole.getY() + this.height.getValue() - mc.getRenderManager().viewerPosY, hole.getZ() + 1 - mc.getRenderManager().viewerPosZ)).forEach(axisAlignedBB -> RenderUtil.drawBox(axisAlignedBB, bedrockR, bedrockG, bedrockB, bedrockA, this.box.getValue(), this.outline.getValue()));
        this.obsidianHoles.stream().map(hole -> new AxisAlignedBB(hole.getX() - mc.getRenderManager().viewerPosX, hole.getY() - mc.getRenderManager().viewerPosY, hole.getZ() - mc.getRenderManager().viewerPosZ, hole.getX() + 1 - mc.getRenderManager().viewerPosX, hole.getY() + this.height.getValue() - mc.getRenderManager().viewerPosY, hole.getZ() + 1 - mc.getRenderManager().viewerPosZ)).forEach(axisAlignedBB -> RenderUtil.drawBox(axisAlignedBB, obsidianR, obsidianG, obsidianB, obsidianA, this.box.getValue(), this.outline.getValue()));
        if (!this.wide.getValue()) return;
        this.doubleBedrockHolesX.stream().map(hole -> new AxisAlignedBB(hole.getX() - mc.getRenderManager().viewerPosX, hole.getY() - mc.getRenderManager().viewerPosY, hole.getZ() - mc.getRenderManager().viewerPosZ, hole.getX() + 2 - mc.getRenderManager().viewerPosX, hole.getY() + this.height.getValue() - mc.getRenderManager().viewerPosY, hole.getZ() + 1 - mc.getRenderManager().viewerPosZ)).forEach(axisAlignedBB -> RenderUtil.drawBox(axisAlignedBB, bedrockR, bedrockG, bedrockB, bedrockA, this.box.getValue(), this.outline.getValue()));
        this.doubleBedrockHolesZ.stream().map(hole -> new AxisAlignedBB(hole.getX() - mc.getRenderManager().viewerPosX, hole.getY() - mc.getRenderManager().viewerPosY, hole.getZ() - mc.getRenderManager().viewerPosZ, hole.getX() + 1 - mc.getRenderManager().viewerPosX, hole.getY() + this.height.getValue() - mc.getRenderManager().viewerPosY, hole.getZ() + 2 - mc.getRenderManager().viewerPosZ)).forEach(axisAlignedBB -> RenderUtil.drawBox(axisAlignedBB, bedrockR, bedrockG, bedrockB, bedrockA, this.box.getValue(), this.outline.getValue()));
        this.doubleObsidianHolesX.stream().map(hole -> new AxisAlignedBB(hole.getX() - mc.getRenderManager().viewerPosX, hole.getY() - mc.getRenderManager().viewerPosY, hole.getZ() - mc.getRenderManager().viewerPosZ, hole.getX() + 2 - mc.getRenderManager().viewerPosX, hole.getY() + this.height.getValue() - mc.getRenderManager().viewerPosY, hole.getZ() + 1 - mc.getRenderManager().viewerPosZ)).forEach(axisAlignedBB -> RenderUtil.drawBox(axisAlignedBB, obsidianR, obsidianG, obsidianB, obsidianA, this.box.getValue(), this.outline.getValue()));
        this.doubleObsidianHolesZ.stream().map(hole -> new AxisAlignedBB(hole.getX() - mc.getRenderManager().viewerPosX, hole.getY() - mc.getRenderManager().viewerPosY, hole.getZ() - mc.getRenderManager().viewerPosZ, hole.getX() + 1 - mc.getRenderManager().viewerPosX, hole.getY() + this.height.getValue() - mc.getRenderManager().viewerPosY, hole.getZ() + 2 - mc.getRenderManager().viewerPosZ)).forEach(axisAlignedBB -> RenderUtil.drawBox(axisAlignedBB, obsidianR, obsidianG, obsidianB, obsidianA, this.box.getValue(), this.outline.getValue()));
    }
}
