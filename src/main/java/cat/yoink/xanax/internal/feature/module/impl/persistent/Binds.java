package cat.yoink.xanax.internal.feature.module.impl.persistent;

import cat.yoink.xanax.internal.feature.module.ModuleCategory;
import cat.yoink.xanax.internal.feature.module.ModuleManager;
import cat.yoink.xanax.internal.feature.module.main.BasicModule;
import cat.yoink.xanax.internal.feature.module.main.ModuleData;
import cat.yoink.xanax.internal.feature.module.state.StateModule;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

/**
 * @author yoink
 */
@ModuleData(
        name = "Binds",
        aliases = {"Binds", "Keybinds"},
        category = ModuleCategory.CLIENT,
        hidden = true,
        description = "Binds for all the modules"
)
public final class Binds extends BasicModule
{
    @SubscribeEvent
    public void onInputKeyInput(InputEvent.KeyInputEvent event)
    {
        if (Keyboard.getEventKeyState() && Keyboard.getEventKey() != Keyboard.KEY_NONE)
            ModuleManager.INSTANCE.getFilteredRegistry().stream()
                    .filter(module -> module.getBind() == Keyboard.getEventKey())
                    .forEach(StateModule::toggle);
    }
}

