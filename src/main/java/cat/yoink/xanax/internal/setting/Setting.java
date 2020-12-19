package cat.yoink.xanax.internal.setting;

import cat.yoink.xanax.internal.module.main.Module;
import cat.yoink.xanax.internal.setting.annotation.Name;
import cat.yoink.xanax.internal.setting.reflect.Reflection;
import cat.yoink.xanax.internal.traits.Nameable;

import java.lang.reflect.Field;

/**
 * @author yoink
 */
public abstract class Setting<T> implements Nameable, ISetting<T>
{
    private final String name;
    private final Module module;
    private final Field field;

    public Setting(Module module, Field field)
    {
        this.name = field.getAnnotation(Name.class).value();
        this.field = field;
        this.module = module;
    }

    @Override
    public final String getName()
    {
        return name;
    }

    @Override
    public Field getField()
    {
        return field;
    }

    @Override
    public Module getModule()
    {
        return module;
    }

    protected void update()
    {
        Reflection.INSTANCE.setValue(module, this, getValue());
    }
}
