package cat.yoink.xanax.internal.feature.command.main;

/**
 * @author yoink
 */
public interface Command
{
    String getName();

    String[] getAliases();

    String getUsage();

    String getDescription();
}
