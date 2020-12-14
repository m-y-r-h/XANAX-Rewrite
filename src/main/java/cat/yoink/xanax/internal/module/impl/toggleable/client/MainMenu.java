package cat.yoink.xanax.internal.module.impl.toggleable.client;

import cat.yoink.xanax.internal.module.ModuleCategory;
import cat.yoink.xanax.internal.module.main.ModuleData;
import cat.yoink.xanax.internal.module.state.StateModule;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@ModuleData(name = "MainMenu", category = ModuleCategory.CLIENT, description = "Custom main menu", enabled = true)
public final class MainMenu extends StateModule
{
    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event)
    {
        if (event.getGui() instanceof GuiMainMenu) event.setGui(cat.yoink.xanax.internal.guiscreen.main.MainMenu.INSTANCE);
    }
}
