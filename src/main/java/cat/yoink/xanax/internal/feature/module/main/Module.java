package cat.yoink.xanax.internal.feature.module.main;

import cat.yoink.xanax.internal.feature.module.ModuleCategory;
import cat.yoink.xanax.internal.setting.Setting;

import java.util.List;

/**
 * @author yoink
 */
public interface Module
{
    String[] getAliases();

    ModuleCategory getCategory();

    int getBind();

    void setBind(int bind);

    boolean isHidden();

    boolean noSave();

    List<Setting<?>> getSettings();
}
