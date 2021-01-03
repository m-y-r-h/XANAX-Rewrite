package cat.yoink.xanax.internal.setting.types;

import cat.yoink.xanax.internal.feature.module.main.BasicModule;
import cat.yoink.xanax.internal.setting.BasicSetting;

import java.awt.*;
import java.lang.reflect.Field;

/**
 * @author yoink
 */
public final class ColorSetting extends BasicSetting<Color>
{
    private Color value;

    public ColorSetting(Field field, BasicModule module, Color value)
    {
        super(module, field);
        this.value = value;
    }

    @Override
    public Color getValue()
    {
        return value;
    }

    @Override
    public void setValue(BasicModule module, Color value)
    {
        this .value = value;
        update();
    }
}
