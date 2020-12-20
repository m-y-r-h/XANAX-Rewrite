package cat.yoink.xanax.internal.module.impl.toggleable.render;

import cat.yoink.xanax.internal.event.impl.RenderHandEvent;
import cat.yoink.xanax.internal.module.ModuleCategory;
import cat.yoink.xanax.internal.module.main.ModuleData;
import cat.yoink.xanax.internal.module.state.StateModule;
import cat.yoink.xanax.internal.setting.annotation.Setting;
import cat.yoink.xanax.internal.setting.annotation.setting.Boolean;
import cat.yoink.xanax.internal.setting.annotation.setting.List;
import cat.yoink.xanax.internal.setting.annotation.setting.Number;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

/**
 * @author yoink
 * @author neko
 */
@ModuleData(name = "ViewModel", category = ModuleCategory.RENDER, description = "Changes your view model")
public final class ViewModel extends StateModule
{
    @Setting(name = "ArmPitch") @Boolean(false) public boolean armPitch;
    @Setting(name = "ArmPitchValue") @Number(value = 90, min = -750, max = 750, increment = 10) public double armPitchValue;
    @Setting(name = "Swing") @Boolean(false) public boolean swing;
    @Setting(name = "SwingValue") @Number(value = 0.86, max = 1, increment = 0.01) public double swingValue;
    @Setting(name = "FOV") @Boolean(false) public boolean fov;
    @Setting(name = "FovMode") @List({"Hard", "Soft"}) public String fovMode;
    @Setting(name = "FovValue") @Number(value = 120, min = 80, max = 180) public double fovValue;
    @Setting(name = "MoveHand") @Boolean(false) public boolean moveHand;
    @Setting(name = "MainX") @Number(value = 0, min = -1, max = 1, increment = 0.01) public double mainX;
    @Setting(name = "MainY") @Number(value = 0, min = -1, max = 1, increment = 0.01) public double mainY;
    @Setting(name = "MainZ") @Number(value = 0, min = -1, max = 1, increment = 0.01) public double mainZ;
    @Setting(name = "OffX") @Number(value = 0, min = -1, max = 1, increment = 0.01) public double offX;
    @Setting(name = "OffY") @Number(value = 0, min = -1, max = 1, increment = 0.01) public double offY;
    @Setting(name = "OffZ") @Number(value = 0, min = -1, max = 1, increment = 0.01) public double offZ;
    @Setting(name = "Animations") @Boolean(false) public boolean animations;
    private float oldFOV;

    @Override
    public void onEnable()
    {
        oldFOV = mc.gameSettings.fovSetting;
    }

    @Override
    public void onDisable()
    {
        mc.gameSettings.fovSetting = oldFOV;
    }

    @SubscribeEvent
    public void onTickClientTick(TickEvent.ClientTickEvent event)
    {
        if (isSafe())
        {
            if (fov && fovMode.equals("Hard")) mc.gameSettings.fovSetting = (float) fovValue;
            else mc.gameSettings.fovSetting = oldFOV;

            if (armPitch) mc.player.renderArmPitch = (float) armPitchValue;

            if (swing) mc.player.swingProgress = (float) swingValue;

            if (animations)
            {
                if (mc.entityRenderer.itemRenderer.prevEquippedProgressMainHand >= 0.9)
                {
                    mc.entityRenderer.itemRenderer.equippedProgressMainHand = 1;
                    mc.entityRenderer.itemRenderer.itemStackMainHand = mc.player.getHeldItem(EnumHand.MAIN_HAND);
                }
                if (mc.entityRenderer.itemRenderer.prevEquippedProgressOffHand >= 0.9)
                {
                    mc.entityRenderer.itemRenderer.equippedProgressOffHand = 1;
                    mc.entityRenderer.itemRenderer.itemStackOffHand = mc.player.getHeldItem(EnumHand.OFF_HAND);
                }
            }
        }
    }

    @SubscribeEvent
    public void onEntityViewRenderFOVModifier(EntityViewRenderEvent.FOVModifier event)
    {
        if (isSafe() && fov && fovMode.equals("Soft"))
            event.setFOV((float) fovValue);
    }

    @SubscribeEvent
    public void onRenderHand(RenderHandEvent event)
    {
        if (isSafe() && moveHand)
        {
            if (event.getSide() == EnumHandSide.RIGHT) GlStateManager.translate(mainX, mainY, mainZ);
            else if (event.getSide() == EnumHandSide.LEFT) GlStateManager.translate(offX, offY, offZ);
        }
    }
}
