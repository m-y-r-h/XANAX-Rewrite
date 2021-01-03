package cat.yoink.xanax.internal.feature.module.main;

import cat.yoink.xanax.internal.feature.module.ModuleCategory;
import cat.yoink.xanax.internal.setting.BasicSetting;

import java.util.List;

/**
 * @author yoink
 */
public interface Module
{
    String getName();

    ModuleCategory getCategory();

    int getBind();

    void setBind(int bind);

    List<BasicSetting<?>> getSettings();
}
