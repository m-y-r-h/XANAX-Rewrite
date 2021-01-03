package cat.yoink.xanax.internal.setting.types;

import cat.yoink.xanax.internal.feature.module.main.BasicModule;
import cat.yoink.xanax.internal.setting.BasicSetting;

import java.lang.reflect.Field;

/**
 * @author yoink
 */
public final class NumberSetting extends BasicSetting<Double>
{
    private final double minimum;
    private final double maximum;
    private final double increment;
    private double value;

    public NumberSetting(Field field, BasicModule module, double value, double minimum, double maximum, double increment)
    {
        super(module, field);
        this.minimum = minimum;
        this.maximum = maximum;
        this.increment = increment;
        this.value = value;
    }

    @Override
    public Double getValue()
    {
        return value;
    }

    @Override
    public void setValue(BasicModule module, Double value)
    {
        double precision = 1 / this.increment;
        this.value = Math.round(Math.max(this.minimum, Math.min(this.maximum, value)) * precision) / precision;
        update();
    }

    public double getMinimum()
    {
        return minimum;
    }

    public double getMaximum()
    {
        return maximum;
    }

    public double getIncrement()
    {
        return increment;
    }
}
