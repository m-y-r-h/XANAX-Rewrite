package cat.yoink.xanax.internal.feature.command.impl;

import cat.yoink.xanax.internal.feature.command.main.Command;
import cat.yoink.xanax.internal.feature.command.main.CommandData;
import cat.yoink.xanax.internal.feature.module.ModuleManager;
import cat.yoink.xanax.internal.util.ChatUtil;
import org.lwjgl.input.Keyboard;

import java.util.Arrays;
import java.util.Locale;

/**
 * @author yoink
 */
@CommandData(
        name = "bind",
        aliases = {"bind", "b"},
        usage = "bind <module> <key>"
)
public final class Bind extends Command
{
    @Override
    public void run(String... arguments)
    {
        if (arguments.length != 2 && printUsage()) return;

        ModuleManager.INSTANCE.getFilteredRegistry().stream()
                .filter(module -> Arrays.stream(module.getAliases()).anyMatch(arguments[0]::equalsIgnoreCase))
                .forEach(module -> {
                    int key = Keyboard.getKeyIndex(arguments[1].toUpperCase(Locale.ROOT));
                    module.setBind(key);
                    ChatUtil.sendPrivateMessage("Bound " + module.getName() + " to " + Keyboard.getKeyName(key));
                });
    }
}
