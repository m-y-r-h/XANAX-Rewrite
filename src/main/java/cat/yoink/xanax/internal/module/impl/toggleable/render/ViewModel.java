package cat.yoink.xanax.internal.module.impl.toggleable.render;

import cat.yoink.xanax.internal.event.impl.RenderHandEvent;
import cat.yoink.xanax.internal.module.ModuleCategory;
import cat.yoink.xanax.internal.module.main.ModuleData;
import cat.yoink.xanax.internal.module.state.StateModule;
import cat.yoink.xanax.internal.setting.types.ListSetting;
import cat.yoink.xanax.internal.setting.types.NumberSetting;
import cat.yoink.xanax.internal.setting.types.StateSetting;
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
    private final StateSetting armPitch = addSetting(new StateSetting("ArmPitch", false));
    private final NumberSetting armPitchValue = addSetting(new NumberSetting("ArmPitchValue", v -> armPitch.getValue(), 90, -750, 750, 10));
    private final StateSetting swing = addSetting(new StateSetting("Swing", false));
    private final NumberSetting swingValue = addSetting(new NumberSetting("SwingValue", v -> swing.getValue(), 0.86, 0, 1, 0.01));
    private final StateSetting fov = addSetting(new StateSetting("FOV", false));
    private final ListSetting fovMode = addSetting(new ListSetting("FovMode", v -> fov.getValue(), "Hard", "Hard", "Soft"));
    private final NumberSetting fovValue = addSetting(new NumberSetting("FovValue", v -> fov.getValue(), 120, 80, 180, 1));
    private final StateSetting moveHand = addSetting(new StateSetting("MoveHand", false));
    private final NumberSetting mainX = addSetting(new NumberSetting("MainX", v -> moveHand.getValue(), 0, -1, 1, 0.01));
    private final NumberSetting mainY = addSetting(new NumberSetting("MainY", v -> moveHand.getValue(), 0, -1, 1, 0.01));
    private final NumberSetting mainZ = addSetting(new NumberSetting("MainZ", v -> moveHand.getValue(), 0, -1, 1, 0.01));
    private final NumberSetting offX = addSetting(new NumberSetting("OffX", v -> moveHand.getValue(), 0, -1, 1, 0.01));
    private final NumberSetting offY = addSetting(new NumberSetting("OffY", v -> moveHand.getValue(), 0, -1, 1, 0.01));
    private final NumberSetting offZ = addSetting(new NumberSetting("OffZ", v -> moveHand.getValue(), 0, -1, 1, 0.01));
    private final StateSetting animations = addSetting(new StateSetting("Animations", false));
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
            if (fov.getValue() && fovMode.getValue().equals("Hard")) mc.gameSettings.fovSetting = fovValue.getValue().floatValue();
            else mc.gameSettings.fovSetting = oldFOV;

            if (armPitch.getValue()) mc.player.renderArmPitch = armPitchValue.getValue().floatValue();

            if (swing.getValue()) mc.player.swingProgress = swingValue.getValue().floatValue();

            if (animations.getValue())
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
        if (isSafe() && fov.getValue() && fovMode.getValue().equals("Soft"))
            event.setFOV(fovValue.getValue().floatValue());
    }

    @SubscribeEvent
    public void onRenderHand(RenderHandEvent event)
    {
        if (isSafe() && moveHand.getValue())
        {
            if (event.getSide() == EnumHandSide.RIGHT) GlStateManager.translate(mainX.getValue(), mainY.getValue(), mainZ.getValue());
            else if (event.getSide() == EnumHandSide.LEFT) GlStateManager.translate(offX.getValue(), offY.getValue(), offZ.getValue());
        }
    }
}
