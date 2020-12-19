package cat.yoink.xanax.internal.setting.annotation.setting;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author yoink
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Number
{
    double minimum() default 0;

    double maximum() default 10;

    double increment() default 1;

    double value();
}
