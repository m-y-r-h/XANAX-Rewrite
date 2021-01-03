package cat.yoink.xanax.internal.setting;

import cat.yoink.xanax.internal.feature.module.main.BasicModule;

import java.lang.reflect.Field;

/**
 * @author yoink
 */
public interface Setting<T>
{
    T getValue();

    void setValue(BasicModule module, T value);

    Field getField();

    BasicModule getModule();
}
