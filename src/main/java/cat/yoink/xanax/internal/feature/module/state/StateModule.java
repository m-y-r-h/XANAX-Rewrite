package cat.yoink.xanax.internal.feature.module.state;

import cat.yoink.xanax.internal.feature.module.main.BasicModule;
import cat.yoink.xanax.internal.feature.module.main.ModuleData;
import cat.yoink.xanax.internal.traits.interfaces.Toggleable;
import net.minecraftforge.common.MinecraftForge;

/**
 * @author yoink
 */
public abstract class StateModule extends BasicModule implements Toggleable, IState
{
    protected boolean state;

    protected StateModule()
    {
        setState(getClass().getAnnotation(ModuleData.class).enabled());
    }

    public StateModule(ModuleData data)
    {
        super(data);
        setState(data.enabled());
    }

    @Override
    public final void toggle()
    {
        setState(!getState());
    }

    @Override
    public final boolean getState()
    {
        return state;
    }

    @Override
    public final void setState(boolean state)
    {
        if (this.state == state) return;
        this.state = state;
        if (state)
        {
            if (isSafe()) onEnable();
            MinecraftForge.EVENT_BUS.register(this);
        }
        else
        {
            if (isSafe()) onDisable();
            MinecraftForge.EVENT_BUS.unregister(this);
        }

    }
}
