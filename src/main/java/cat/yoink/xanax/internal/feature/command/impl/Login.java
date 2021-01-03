package cat.yoink.xanax.internal.feature.command.impl;

import cat.yoink.xanax.internal.feature.command.main.BasicCommand;
import cat.yoink.xanax.internal.feature.command.main.CommandData;
import cat.yoink.xanax.internal.util.SessionUtil;

/**
 * @author yoink
 */
@CommandData(
        name = "Login",
        aliases = "login",
        usage = "login <email> <pass>"
)
public final class Login extends BasicCommand
{
    @Override
    public void run(String... arguments)
    {
        if (arguments.length != 2 && printUsage()) return;

        mc.session = SessionUtil.getSession(arguments[0], arguments[1]);
    }
}
