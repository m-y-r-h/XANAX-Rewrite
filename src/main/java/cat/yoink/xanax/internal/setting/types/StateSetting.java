package cat.yoink.xanax.internal.setting.types;

import cat.yoink.xanax.internal.feature.module.main.BasicModule;
import cat.yoink.xanax.internal.setting.BasicSetting;

import java.lang.reflect.Field;

/**
 * @author yoink
 */
public final class StateSetting extends BasicSetting<Boolean>
{
    private boolean value;

    public StateSetting(Field field, BasicModule module, boolean value)
    {
        super(module, field);
        this.value = value;
    }

    @Override
    public Boolean getValue()
    {
        return value;
    }

    @Override
    public void setValue(BasicModule module, Boolean value)
    {
        this.value = value;
        update();
    }
}
