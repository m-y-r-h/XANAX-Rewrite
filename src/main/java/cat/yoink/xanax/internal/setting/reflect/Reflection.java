package cat.yoink.xanax.internal.setting.reflect;

import cat.yoink.xanax.internal.module.main.Module;
import cat.yoink.xanax.internal.setting.Setting;
import cat.yoink.xanax.internal.setting.annotation.Name;
import cat.yoink.xanax.internal.setting.annotation.setting.Boolean;
import cat.yoink.xanax.internal.setting.annotation.setting.Color;
import cat.yoink.xanax.internal.setting.annotation.setting.List;
import cat.yoink.xanax.internal.setting.annotation.setting.Number;
import cat.yoink.xanax.internal.setting.types.ColorSetting;
import cat.yoink.xanax.internal.setting.types.ListSetting;
import cat.yoink.xanax.internal.setting.types.NumberSetting;
import cat.yoink.xanax.internal.setting.types.StateSetting;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author yoink
 */
public enum Reflection
{
    INSTANCE;

    public java.util.List<Setting<?>> getSettings(Module instance)
    {
        java.util.List<Setting<?>> settings = new ArrayList<>();
        Arrays.stream(instance.getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Name.class))
                .forEach(field -> {
                    if (field.isAnnotationPresent(Boolean.class)) settings.add(new StateSetting(field, instance, field.getAnnotation(Boolean.class).value()));
                    else if (field.isAnnotationPresent(List.class)) settings.add(new ListSetting(field, instance, field.getAnnotation(List.class).value()));
                    else if (field.isAnnotationPresent(Number.class)) settings.add(new NumberSetting(field, instance, field.getAnnotation(Number.class).value(), field.getAnnotation(Number.class).minimum(), field.getAnnotation(Number.class).maximum(), field.getAnnotation(Number.class).increment()));
                    else if (field.isAnnotationPresent(Color.class)) settings.add(new ColorSetting(field, instance, new java.awt.Color(field.getAnnotation(Color.class).value())));
                });

        return settings;
    }

    public void setValue(Module instance, Setting<?> setting, Object value)
    {
        try
        {
            setting.getField().set(instance, value);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
