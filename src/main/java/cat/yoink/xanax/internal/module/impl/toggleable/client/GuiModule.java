package cat.yoink.xanax.internal.module.impl.toggleable.client;

import cat.yoink.xanax.internal.guiscreen.clickgui.ClickGUI;
import cat.yoink.xanax.internal.module.ModuleCategory;
import cat.yoink.xanax.internal.module.main.ModuleData;
import cat.yoink.xanax.internal.module.state.StateModule;
import cat.yoink.xanax.internal.setting.annotation.Name;
import cat.yoink.xanax.internal.setting.annotation.setting.Boolean;
import cat.yoink.xanax.internal.setting.annotation.setting.List;
import org.lwjgl.input.Keyboard;

/**
 * @author yoink
 */
@ModuleData(name = "ClickGUI", category = ModuleCategory.CLIENT, defaultBind = Keyboard.KEY_RSHIFT, description = "Toggle modules and settings in a gui")
public final class GuiModule extends StateModule
{
    @Name("Outline") @Boolean(false) public boolean outline;
    @Name("Closing") @List({"Keyboard", "Button", "Both"}) public String closing;

    @Override
    public void onEnable()
    {
        mc.displayGuiScreen(ClickGUI.INSTANCE);
    }
}
