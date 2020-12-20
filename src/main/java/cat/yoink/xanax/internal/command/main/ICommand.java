package cat.yoink.xanax.internal.command.main;

public interface ICommand
{
    String[] getAliases();

    String getUsage();
}
