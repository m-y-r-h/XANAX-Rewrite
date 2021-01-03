package cat.yoink.xanax.internal.feature.command.main;

import cat.yoink.xanax.internal.feature.Feature;
import cat.yoink.xanax.internal.feature.command.CommandManager;
import cat.yoink.xanax.internal.traits.interfaces.Describable;
import cat.yoink.xanax.internal.traits.interfaces.Nameable;
import cat.yoink.xanax.internal.traits.interfaces.Runnable;
import cat.yoink.xanax.internal.traits.manager.Manager;
import cat.yoink.xanax.internal.util.ChatUtil;

/**
 * @author yoink
 */
public abstract class BasicCommand implements Nameable, Describable, Runnable, Feature, Command
{
    protected final String name;
    protected final String[] aliases;
    protected final String usage;
    protected final String description;

    protected BasicCommand()
    {
        name = getClass().getAnnotation(CommandData.class).name();
        aliases = getClass().getAnnotation(CommandData.class).aliases();
        usage = getClass().getAnnotation(CommandData.class).usage();
        description = getClass().getAnnotation(CommandData.class).description();
    }

    public BasicCommand(CommandData data)
    {
        name = data.name();
        aliases = data.aliases();
        usage = data.usage();
        description = data.description();
    }

    protected final boolean printUsage()
    {
        ChatUtil.sendPrivateMessage("Usage: " + usage);
        return true;
    }

    @Override
    public Manager<?> getManager()
    {
        return CommandManager.INSTANCE;
    }

    @Override
    public final String getName()
    {
        return name;
    }

    @Override
    public final String[] getAliases()
    {
        return aliases;
    }

    @Override
    public final String getUsage()
    {
        return usage;
    }

    @Override
    public final String getDescription()
    {
        return description;
    }
}
