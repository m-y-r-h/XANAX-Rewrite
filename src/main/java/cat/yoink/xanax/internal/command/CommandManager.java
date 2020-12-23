package cat.yoink.xanax.internal.command;

import cat.yoink.xanax.internal.XANAX;
import cat.yoink.xanax.internal.command.impl.Config;
import cat.yoink.xanax.internal.command.impl.Login;
import cat.yoink.xanax.internal.command.impl.Prefix;
import cat.yoink.xanax.internal.command.impl.Toggle;
import cat.yoink.xanax.internal.command.main.Command;
import cat.yoink.xanax.internal.traits.Configurable;
import cat.yoink.xanax.internal.traits.Minecraft;
import cat.yoink.xanax.internal.util.ChatUtil;
import cat.yoink.xanax.internal.util.FileUtil;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraftforge.client.event.ClientChatEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author yoink
 */
public enum CommandManager implements Configurable, Minecraft
{
    INSTANCE;

    private String prefix = ".";
    private final List<Command> commands = new ArrayList<>();

    CommandManager()
    {
        addCommands(new Prefix(), new Login(), new Toggle(),
                new Config());
    }

    public void parseCommand(ClientChatEvent event)
    {
        if (!event.getMessage().startsWith(prefix)) return;
        event.setCanceled(true);
        ChatUtil.addSendMessage(event.getMessage());

        String[] split = event.getMessage().split(" ");
        commands.stream().filter(c -> Arrays.stream(c.getAliases()).anyMatch(s -> s.equalsIgnoreCase(split[0].substring(prefix.length()))))
                .forEach(c -> c.run(Arrays.copyOfRange(split, 1, split.length)));
    }

    @Override
    public boolean save(String name)
    {
        JsonObject config = new JsonObject();
        config.addProperty("Prefix", prefix);
        return FileUtil.saveFile(new File(directory.getAbsolutePath() + File.separator + name, "Prefix.json"), XANAX.INSTANCE.gson.toJson(config));
    }

    @Override
    public boolean load(String name)
    {
        String contents = FileUtil.getContents(new File(directory.getAbsolutePath() + File.separator + name, "Prefix.json"));
        if (contents.equals("")) return false;
        JsonObject parser = new JsonParser().parse(contents).getAsJsonObject();
        prefix = parser.get("Prefix").getAsString();
        return true;
    }

    private void addCommands(Command... commands)
    {
        this.commands.addAll(Arrays.asList(commands));
    }

    public List<Command> getCommands()
    {
        return commands;
    }

    public String getPrefix()
    {
        return prefix;
    }

    public void setPrefix(String prefix)
    {
        this.prefix = prefix;
    }
}
