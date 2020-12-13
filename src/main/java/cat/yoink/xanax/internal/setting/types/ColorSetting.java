package cat.yoink.xanax.internal.setting.types;

import cat.yoink.xanax.internal.setting.Setting;

import java.awt.*;
import java.util.function.Predicate;

/**
 * @author yoink
 */
public final class ColorSetting extends Setting<Color>
{
    private Color value;

    public ColorSetting(String name, Color value)
    {
        super(name);
        this.value = value;
    }

    public ColorSetting(String name, Predicate<Setting<Color>> visible, Color value)
    {
        super(name, visible);
        this.value = value;
    }

    @Override
    public Color getValue()
    {
        return value;
    }

    @Override
    public void setValue(Color value)
    {
        this.value = value;
    }
}
