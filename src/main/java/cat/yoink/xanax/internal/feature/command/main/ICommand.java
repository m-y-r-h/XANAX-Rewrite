package cat.yoink.xanax.internal.feature.command.main;

/**
 * @author yoink
 */
public interface ICommand
{
    String[] getAliases();

    String getUsage();
}
