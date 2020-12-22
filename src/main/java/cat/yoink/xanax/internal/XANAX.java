package cat.yoink.xanax.internal;

import cat.yoink.xanax.internal.command.CommandManager;
import cat.yoink.xanax.internal.module.ModuleManager;
import cat.yoink.xanax.internal.traits.Configurable;
import cat.yoink.xanax.internal.traits.Nameable;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author yoink
 */
public enum XANAX implements Configurable, Runnable, Nameable
{
    INSTANCE;

    public Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public void run()
    {
        try { load(); } catch (Exception ignored) { }
        Runtime.getRuntime().addShutdownHook(new Thread(this::save));
    }

    @Override
    public void save()
    {
        if (!directory.exists() && !directory.mkdirs()) return;

        ModuleManager.INSTANCE.save();
        CommandManager.INSTANCE.save();
    }

    @Override
    public void load()
    {
        if (!directory.exists() && !directory.mkdirs()) return;

        ModuleManager.INSTANCE.load();
        CommandManager.INSTANCE.load();
    }

    @Override
    public String getName()
    {
        return "XANAX";
    }
}
