package cat.yoink.xanax.internal.setting.types;

import cat.yoink.xanax.internal.setting.Setting;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author yoink
 */
public final class ListSetting extends Setting<String>
{
    private final List<String> values;
    private int index;

    public ListSetting(String name, String defaultValue, String... values)
    {
        super(name);
        this.values = Arrays.asList(values);
        this.index = this.values.indexOf(defaultValue);
    }

    public ListSetting(String name, Predicate<Setting<String>> visible, String defaultValue, String... values)
    {
        super(name, visible);
        this.values = Arrays.asList(values);
        this.index = this.values.indexOf(defaultValue);
    }

    @Override
    public String getValue()
    {
        return values.get(index);
    }

    @Override
    public void setValue(String value)
    {
        index = values.indexOf(value);
    }

    public void cycleForward()
    {
        if (index < values.size() - 1) index++;
        else index = 0;
    }

    public void cycleBackward()
    {
        if (index > 0) index--;
        else index = values.size() - 1;
    }
}
