package cat.yoink.xanax.internal.module.impl.toggleable.world;

import cat.yoink.xanax.internal.event.impl.CollisionEvent;
import cat.yoink.xanax.internal.event.impl.WaterPushEvent;
import cat.yoink.xanax.internal.module.ModuleCategory;
import cat.yoink.xanax.internal.module.main.ModuleData;
import cat.yoink.xanax.internal.module.state.StateModule;
import cat.yoink.xanax.internal.setting.types.ListSetting;
import cat.yoink.xanax.internal.setting.types.NumberSetting;
import cat.yoink.xanax.internal.setting.types.StateSetting;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import javax.swing.plaf.nimbus.State;

@Mod.EventBusSubscriber
@ModuleData(name = "ItemFov", category = ModuleCategory.RENDER, description = "Change ur item model location")
public final class ItemFov extends StateModule
{

    private final NumberSetting itemfov = addSetting(new NumberSetting("ItemFov", 130, 110, 170, 10));


    @SubscribeEvent
    public void onFov(EntityViewRenderEvent.FOVModifier event) {
        event.setFOV(itemfov.getValue().floatValue());
    }

    public void onEnable(){
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void onDisable(){
        MinecraftForge.EVENT_BUS.unregister(this);
    }

}


