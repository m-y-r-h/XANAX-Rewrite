package cat.yoink.xanax.internal.command.impl;

import cat.yoink.xanax.internal.XANAX;
import cat.yoink.xanax.internal.command.main.Command;
import cat.yoink.xanax.internal.command.main.CommandData;

/**
 * @author yoink
 */
@CommandData(name = "Config", aliases = "config", usage = "config <load|save> <name>")
public final class Config extends Command
{
    @Override
    public void run(String... arguments)
    {
        if (arguments.length != 2 && printUsage()) return;

        String type = arguments[0];

        if (type.equalsIgnoreCase("load")) XANAX.INSTANCE.load(arguments[1]);
        else if (type.equalsIgnoreCase("save")) XANAX.INSTANCE.save(arguments[1]);
        else printUsage();
    }
}
