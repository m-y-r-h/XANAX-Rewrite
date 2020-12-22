package cat.yoink.xanax.internal.command;

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
        addCommands(new Prefix(), new Login(), new Toggle());
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
    public void save()
    {
        JsonObject config = new JsonObject();
        config.addProperty("Prefix", prefix);
        FileUtil.saveFile(new File(directory.getAbsolutePath(), "Prefix.json"), config.toString());
    }

    @Override
    public void load()
    {
        JsonObject parser = new JsonParser().parse(FileUtil.getContents(new File(directory.getAbsolutePath(), "Prefix.json"))).getAsJsonObject();
        prefix = parser.get("Prefix").getAsString();
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
