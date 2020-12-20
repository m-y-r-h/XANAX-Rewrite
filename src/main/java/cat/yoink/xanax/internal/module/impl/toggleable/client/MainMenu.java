package cat.yoink.xanax.internal.module.impl.toggleable.client;

import cat.yoink.xanax.internal.module.ModuleCategory;
import cat.yoink.xanax.internal.module.main.ModuleData;
import cat.yoink.xanax.internal.module.state.StateModule;
import cat.yoink.xanax.internal.setting.annotation.Setting;
import cat.yoink.xanax.internal.setting.annotation.setting.List;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author yoink
 */
@ModuleData(name = "MainMenu", category = ModuleCategory.CLIENT, description = "Custom main menu", enabled = true)
public final class MainMenu extends StateModule
{
    @Setting(name = "Mode", description = "Choose which background to use") @List({"Minecraft", "Color"}) public String mode;

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event)
    {
        if (event.getGui() instanceof GuiMainMenu) event.setGui(cat.yoink.xanax.internal.guiscreen.main.MainMenu.INSTANCE);
    }
}
