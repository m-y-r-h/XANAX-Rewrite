package cat.yoink.xanax.internal.module.state;

import cat.yoink.xanax.internal.module.main.Module;
import cat.yoink.xanax.internal.module.main.ModuleData;
import net.minecraftforge.common.MinecraftForge;

/**
 * @author yoink
 */
public abstract class StateModule extends Module
{
    protected boolean enabled;

    public StateModule()
    {
        setEnabled(getClass().getAnnotation(ModuleData.class).enabled());
    }

    public final void toggle()
    {
        setEnabled(!isEnabled());
    }

    public final boolean isEnabled()
    {
        return enabled;
    }

    public final void setEnabled(boolean enabled)
    {
        if (enabled)
        {
            if (isSafe()) onEnable();
            MinecraftForge.EVENT_BUS.register(this);
        }
        else
        {
            if (isSafe()) onDisable();
            MinecraftForge.EVENT_BUS.unregister(this);
        }
        this.enabled = enabled;
    }

    public void onEnable() { }
    public void onDisable() { }
}
