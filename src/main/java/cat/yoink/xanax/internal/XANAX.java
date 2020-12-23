package cat.yoink.xanax.internal;

import cat.yoink.xanax.internal.command.CommandManager;
import cat.yoink.xanax.internal.module.ModuleManager;
import cat.yoink.xanax.internal.traits.Configurable;
import cat.yoink.xanax.internal.traits.Nameable;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;

/**
 * @author yoink
 */
public enum XANAX implements Configurable, Runnable, Nameable
{
    INSTANCE;

    public Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public void run()
    {
        try { load("main"); } catch (Exception ignored) { }
        Runtime.getRuntime().addShutdownHook(new Thread(() -> save("main")));
    }

    @Override
    public boolean save(String name)
    {
        File dir = new File(directory + File.separator + name);
        if (!dir.exists() && !dir.mkdirs()) return false;

        return ModuleManager.INSTANCE.save(name) && CommandManager.INSTANCE.save(name);
    }

    @Override
    public boolean load(String name)
    {
        if (!new File(directory + File.separator + name).exists()) return false;

        return ModuleManager.INSTANCE.load(name) && CommandManager.INSTANCE.load(name);
    }

    @Override
    public String getName()
    {
        return "XANAX";
    }
}
