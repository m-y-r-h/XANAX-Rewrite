package cat.yoink.xanax.internal.module.impl.toggleable.render;

import cat.yoink.xanax.internal.module.ModuleCategory;
import cat.yoink.xanax.internal.module.main.ModuleData;
import cat.yoink.xanax.internal.module.state.StateModule;
import cat.yoink.xanax.internal.setting.annotation.Name;
import cat.yoink.xanax.internal.setting.annotation.setting.Boolean;
import cat.yoink.xanax.internal.setting.annotation.setting.Color;
import cat.yoink.xanax.internal.setting.annotation.setting.Number;
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
    @Name("Bedrock") @Color(-65536) public java.awt.Color bedrockColor;
    @Name("Obsidian") @Color(-16711936) public java.awt.Color obsidianColor;
    @Name("BedrockAlpha") @Number(value = 150, max = 255) public double bedrockAlpha;
    @Name("ObsidianAlpha") @Number(value = 150, max = 255) public double obsidianAlpha;
    @Name("Height") @Number(value = 0.1, min = -1, max = 1, increment = 0.1) public double height;
    @Name("Range") @Number(value = 8, min = 2, max = 20) public double range;
    @Name("Performance") @Number(value = 5, min = 1, max = 20) public double performance;
    @Name("Box") @Boolean(true) public boolean box;
    @Name("Outline") @Boolean(true) public boolean outline;
    @Name("Wide") @Boolean(true) public boolean wide;

    private final List<BlockPos> bedrockHoles = new ArrayList<>();
    private final List<BlockPos> obsidianHoles = new ArrayList<>();
    private final List<BlockPos> doubleBedrockHolesX = new ArrayList<>();
    private final List<BlockPos> doubleBedrockHolesZ = new ArrayList<>();
    private final List<BlockPos> doubleObsidianHolesX = new ArrayList<>();
    private final List<BlockPos> doubleObsidianHolesZ = new ArrayList<>();

    @SubscribeEvent
    public void onTickClientTick(TickEvent.ClientTickEvent event)
    {
        if (isSafe() && mc.player.ticksExisted % performance == 0)
        {
            bedrockHoles.clear();
            obsidianHoles.clear();
            doubleBedrockHolesX.clear();
            doubleBedrockHolesZ.clear();
            doubleObsidianHolesX.clear();
            doubleObsidianHolesZ.clear();

            assert mc.renderViewEntity != null;
            Vec3i vec3i = new Vec3i(mc.renderViewEntity.posX, mc.renderViewEntity.posY, mc.renderViewEntity.posZ);

            for (int i = vec3i.getX() - (int) range; i < vec3i.getX() + range; ++i)
            {
                for (int j = vec3i.getZ() - (int) range; j < vec3i.getZ() + range; ++j)
                {
                    for (int k = vec3i.getY() + (int) range; k > vec3i.getY() - range; --k)
                    {
                        BlockPos blockPos = new BlockPos(i, k, j);

                        if (WorldUtil.isBedrockHole(blockPos)) bedrockHoles.add(blockPos);
                        else if (WorldUtil.isHole(blockPos)) obsidianHoles.add(blockPos);
                        else if (wide)
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
        int bedrockR = bedrockColor.getRed();
        int bedrockG = bedrockColor.getGreen();
        int bedrockB = bedrockColor.getBlue();
        int bedrockA = (int) bedrockAlpha;
        int obsidianR = obsidianColor.getRed();
        int obsidianG = obsidianColor.getGreen();
        int obsidianB = obsidianColor.getBlue();
        int obsidianA = (int) obsidianAlpha;

        bedrockHoles.stream().map(hole -> new AxisAlignedBB(hole.getX() - mc.getRenderManager().viewerPosX, hole.getY() - mc.getRenderManager().viewerPosY, hole.getZ() - mc.getRenderManager().viewerPosZ, hole.getX() + 1 - mc.getRenderManager().viewerPosX, hole.getY() + height - mc.getRenderManager().viewerPosY, hole.getZ() + 1 - mc.getRenderManager().viewerPosZ)).forEach(axisAlignedBB -> RenderUtil.drawBox(axisAlignedBB, bedrockR, bedrockG, bedrockB, bedrockA, box, outline));
        obsidianHoles.stream().map(hole -> new AxisAlignedBB(hole.getX() - mc.getRenderManager().viewerPosX, hole.getY() - mc.getRenderManager().viewerPosY, hole.getZ() - mc.getRenderManager().viewerPosZ, hole.getX() + 1 - mc.getRenderManager().viewerPosX, hole.getY() + height - mc.getRenderManager().viewerPosY, hole.getZ() + 1 - mc.getRenderManager().viewerPosZ)).forEach(axisAlignedBB -> RenderUtil.drawBox(axisAlignedBB, obsidianR, obsidianG, obsidianB, obsidianA, box, outline));
        if (!wide) return;
        doubleBedrockHolesX.stream().map(hole -> new AxisAlignedBB(hole.getX() - mc.getRenderManager().viewerPosX, hole.getY() - mc.getRenderManager().viewerPosY, hole.getZ() - mc.getRenderManager().viewerPosZ, hole.getX() + 2 - mc.getRenderManager().viewerPosX, hole.getY() + height - mc.getRenderManager().viewerPosY, hole.getZ() + 1 - mc.getRenderManager().viewerPosZ)).forEach(axisAlignedBB -> RenderUtil.drawBox(axisAlignedBB, bedrockR, bedrockG, bedrockB, bedrockA, box, outline));
        doubleBedrockHolesZ.stream().map(hole -> new AxisAlignedBB(hole.getX() - mc.getRenderManager().viewerPosX, hole.getY() - mc.getRenderManager().viewerPosY, hole.getZ() - mc.getRenderManager().viewerPosZ, hole.getX() + 1 - mc.getRenderManager().viewerPosX, hole.getY() + height - mc.getRenderManager().viewerPosY, hole.getZ() + 2 - mc.getRenderManager().viewerPosZ)).forEach(axisAlignedBB -> RenderUtil.drawBox(axisAlignedBB, bedrockR, bedrockG, bedrockB, bedrockA, box, outline));
        doubleObsidianHolesX.stream().map(hole -> new AxisAlignedBB(hole.getX() - mc.getRenderManager().viewerPosX, hole.getY() - mc.getRenderManager().viewerPosY, hole.getZ() - mc.getRenderManager().viewerPosZ, hole.getX() + 2 - mc.getRenderManager().viewerPosX, hole.getY() + height - mc.getRenderManager().viewerPosY, hole.getZ() + 1 - mc.getRenderManager().viewerPosZ)).forEach(axisAlignedBB -> RenderUtil.drawBox(axisAlignedBB, obsidianR, obsidianG, obsidianB, obsidianA, box, outline));
        doubleObsidianHolesZ.stream().map(hole -> new AxisAlignedBB(hole.getX() - mc.getRenderManager().viewerPosX, hole.getY() - mc.getRenderManager().viewerPosY, hole.getZ() - mc.getRenderManager().viewerPosZ, hole.getX() + 1 - mc.getRenderManager().viewerPosX, hole.getY() + height - mc.getRenderManager().viewerPosY, hole.getZ() + 2 - mc.getRenderManager().viewerPosZ)).forEach(axisAlignedBB -> RenderUtil.drawBox(axisAlignedBB, obsidianR, obsidianG, obsidianB, obsidianA, box, outline));
    }
}
