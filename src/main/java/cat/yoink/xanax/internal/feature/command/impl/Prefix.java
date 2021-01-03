package cat.yoink.xanax.internal.feature.command.impl;

import cat.yoink.xanax.internal.feature.command.CommandManager;
import cat.yoink.xanax.internal.feature.command.main.BasicCommand;
import cat.yoink.xanax.internal.feature.command.main.CommandData;
import cat.yoink.xanax.internal.util.ChatUtil;

/**
 * @author yoink
 */
@CommandData(
        name = "Prefix",
        aliases = "prefix",
        usage = "prefix <char>"
)
public final class Prefix extends BasicCommand
{
    @Override
    public void run(String... args)
    {
        if (args.length == 0 && printUsage()) return;

        CommandManager.INSTANCE.setPrefix(args[0]);
        ChatUtil.sendPrivateMessage("Set prefix to " + args[0]);
    }
}
