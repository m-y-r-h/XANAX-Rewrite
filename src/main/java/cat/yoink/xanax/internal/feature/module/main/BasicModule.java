package cat.yoink.xanax.internal.feature.module.main;

import cat.yoink.xanax.internal.feature.Feature;
import cat.yoink.xanax.internal.feature.module.ModuleCategory;
import cat.yoink.xanax.internal.feature.module.ModuleManager;
import cat.yoink.xanax.internal.setting.BasicSetting;
import cat.yoink.xanax.internal.setting.reflect.Reflection;
import cat.yoink.xanax.internal.traits.interfaces.Describable;
import cat.yoink.xanax.internal.traits.interfaces.Nameable;
import cat.yoink.xanax.internal.traits.manager.Manager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yoink
 */
public abstract class BasicModule implements Feature, Describable, Nameable, Module
{
    protected final String name;
    protected final String[] aliases;
    protected final ModuleCategory category;
    protected final String description;
    protected final boolean hidden;
    protected int bind;
    protected boolean noSave;
    protected final List<BasicSetting<?>> settings;

    protected BasicModule()
    {
        name = getClass().getAnnotation(ModuleData.class).name();
        aliases = getClass().getAnnotation(ModuleData.class).aliases();
        category = getClass().getAnnotation(ModuleData.class).category();
        description = getClass().getAnnotation(ModuleData.class).description();
        hidden = getClass().getAnnotation(ModuleData.class).hidden();
        bind = getClass().getAnnotation(ModuleData.class).defaultBind();
        noSave = getClass().getAnnotation(ModuleData.class).noSave();
        settings = new ArrayList<>();
        settings.addAll(Reflection.INSTANCE.getSettings(this));
    }

    public BasicModule(ModuleData data)
    {
        name = data.name();
        aliases = data.aliases();
        category = data.category();
        description = data.description();
        hidden = data.hidden();
        bind = data.defaultBind();
        noSave = data.noSave();
        settings = new ArrayList<>();
        settings.addAll(Reflection.INSTANCE.getSettings(this));
    }

    protected final boolean isSafe()
    {
        return mc.player != null && mc.world != null;
    }

    public final BasicSetting<?> getSetting(String name)
    {
        return settings.stream().filter(setting -> setting.getName().equalsIgnoreCase(name)).findAny().orElse(null);
    }

    @Override
    public final Manager<?> getManager()
    {
        return ModuleManager.INSTANCE;
    }

    @Override
    public final String getName()
    {
        return name;
    }

    public String[] getAliases()
    {
        return aliases;
    }

    @Override
    public final ModuleCategory getCategory()
    {
        return category;
    }

    @Override
    public final String getDescription()
    {
        return description;
    }

    @Override
    public final int getBind()
    {
        return bind;
    }

    @Override
    public final void setBind(int bind)
    {
        this.bind = bind;
    }

    public final boolean isHidden()
    {
        return hidden;
    }

    public final boolean noSave()
    {
        return noSave;
    }

    @Override
    public final List<BasicSetting<?>> getSettings()
    {
        return settings;
    }
}
