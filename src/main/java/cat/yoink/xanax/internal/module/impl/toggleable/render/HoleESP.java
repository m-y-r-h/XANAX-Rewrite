package cat.yoink.xanax.internal.module.impl.toggleable.render;

import cat.yoink.xanax.internal.module.ModuleCategory;
import cat.yoink.xanax.internal.module.main.ModuleData;
import cat.yoink.xanax.internal.module.state.StateModule;
import cat.yoink.xanax.internal.setting.types.ColorSetting;
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

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yoink
 */
@ModuleData(name = "HoleESP", category = ModuleCategory.RENDER, description = "Highlights nearby holes")
public final class HoleESP extends StateModule
{
    private final ColorSetting bedrockColor = addSetting(new ColorSetting("Bedrock", new Color(255, 0, 1)));
    private final ColorSetting obsidianColor = addSetting(new ColorSetting("Obsidian", new Color(0, 255, 0)));
    private final NumberSetting bedrockAlpha = addSetting(new NumberSetting("BedrockAlpha", 150, 0, 255, 1));
    private final NumberSetting obsidianAlpha = addSetting(new NumberSetting("ObsidianAlpha", 150, 0, 255, 1));
    private final NumberSetting height = addSetting(new NumberSetting("Height", 0.1, -1, 1, 0.1));
    private final NumberSetting range = addSetting(new NumberSetting("Range", 8, 2, 20, 1));
    private final NumberSetting performance = addSetting(new NumberSetting("Performance", 5, 1, 20, 1));
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
        if (isSafe() && mc.player.ticksExisted % performance.getValue().intValue() == 0)
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
        int bedrockR = bedrockColor.getValue().getRed();
        int bedrockG = bedrockColor.getValue().getGreen();
        int bedrockB = bedrockColor.getValue().getBlue();
        int bedrockA = bedrockAlpha.getValue().intValue();
        int obsidianR = obsidianColor.getValue().getRed();
        int obsidianG = obsidianColor.getValue().getGreen();
        int obsidianB = obsidianColor.getValue().getBlue();
        int obsidianA = obsidianAlpha.getValue().intValue();

        bedrockHoles.stream().map(hole -> new AxisAlignedBB(hole.getX() - mc.getRenderManager().viewerPosX, hole.getY() - mc.getRenderManager().viewerPosY, hole.getZ() - mc.getRenderManager().viewerPosZ, hole.getX() + 1 - mc.getRenderManager().viewerPosX, hole.getY() + height.getValue() - mc.getRenderManager().viewerPosY, hole.getZ() + 1 - mc.getRenderManager().viewerPosZ)).forEach(axisAlignedBB -> RenderUtil.drawBox(axisAlignedBB, bedrockR, bedrockG, bedrockB, bedrockA, box.getValue(), outline.getValue()));
        obsidianHoles.stream().map(hole -> new AxisAlignedBB(hole.getX() - mc.getRenderManager().viewerPosX, hole.getY() - mc.getRenderManager().viewerPosY, hole.getZ() - mc.getRenderManager().viewerPosZ, hole.getX() + 1 - mc.getRenderManager().viewerPosX, hole.getY() + height.getValue() - mc.getRenderManager().viewerPosY, hole.getZ() + 1 - mc.getRenderManager().viewerPosZ)).forEach(axisAlignedBB -> RenderUtil.drawBox(axisAlignedBB, obsidianR, obsidianG, obsidianB, obsidianA, box.getValue(), outline.getValue()));
        if (!wide.getValue()) return;
        doubleBedrockHolesX.stream().map(hole -> new AxisAlignedBB(hole.getX() - mc.getRenderManager().viewerPosX, hole.getY() - mc.getRenderManager().viewerPosY, hole.getZ() - mc.getRenderManager().viewerPosZ, hole.getX() + 2 - mc.getRenderManager().viewerPosX, hole.getY() + height.getValue() - mc.getRenderManager().viewerPosY, hole.getZ() + 1 - mc.getRenderManager().viewerPosZ)).forEach(axisAlignedBB -> RenderUtil.drawBox(axisAlignedBB, bedrockR, bedrockG, bedrockB, bedrockA, box.getValue(), outline.getValue()));
        doubleBedrockHolesZ.stream().map(hole -> new AxisAlignedBB(hole.getX() - mc.getRenderManager().viewerPosX, hole.getY() - mc.getRenderManager().viewerPosY, hole.getZ() - mc.getRenderManager().viewerPosZ, hole.getX() + 1 - mc.getRenderManager().viewerPosX, hole.getY() + height.getValue() - mc.getRenderManager().viewerPosY, hole.getZ() + 2 - mc.getRenderManager().viewerPosZ)).forEach(axisAlignedBB -> RenderUtil.drawBox(axisAlignedBB, bedrockR, bedrockG, bedrockB, bedrockA, box.getValue(), outline.getValue()));
        doubleObsidianHolesX.stream().map(hole -> new AxisAlignedBB(hole.getX() - mc.getRenderManager().viewerPosX, hole.getY() - mc.getRenderManager().viewerPosY, hole.getZ() - mc.getRenderManager().viewerPosZ, hole.getX() + 2 - mc.getRenderManager().viewerPosX, hole.getY() + height.getValue() - mc.getRenderManager().viewerPosY, hole.getZ() + 1 - mc.getRenderManager().viewerPosZ)).forEach(axisAlignedBB -> RenderUtil.drawBox(axisAlignedBB, obsidianR, obsidianG, obsidianB, obsidianA, box.getValue(), outline.getValue()));
        doubleObsidianHolesZ.stream().map(hole -> new AxisAlignedBB(hole.getX() - mc.getRenderManager().viewerPosX, hole.getY() - mc.getRenderManager().viewerPosY, hole.getZ() - mc.getRenderManager().viewerPosZ, hole.getX() + 1 - mc.getRenderManager().viewerPosX, hole.getY() + height.getValue() - mc.getRenderManager().viewerPosY, hole.getZ() + 2 - mc.getRenderManager().viewerPosZ)).forEach(axisAlignedBB -> RenderUtil.drawBox(axisAlignedBB, obsidianR, obsidianG, obsidianB, obsidianA, box.getValue(), outline.getValue()));
    }
}
