package cat.yoink.xanax.internal.module.impl.toggleable.render;

import cat.yoink.xanax.internal.module.ModuleCategory;
import cat.yoink.xanax.internal.module.main.ModuleData;
import cat.yoink.xanax.internal.module.state.StateModule;
import cat.yoink.xanax.internal.setting.types.NumberSetting;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@ModuleData(name = "ItemFov", category = ModuleCategory.RENDER, description = "Change your fov excluding the item location")
public final class ItemFov extends StateModule
{
    private final NumberSetting itemfov = addSetting(new NumberSetting("ItemFov", 130, 110, 170, 1));

    @SubscribeEvent
    public void onFov(EntityViewRenderEvent.FOVModifier event) 
    {
        event.setFOV(itemfov.getValue().floatValue());
    }
}
