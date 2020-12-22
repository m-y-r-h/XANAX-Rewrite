package cat.yoink.xanax.internal.module.impl.toggleable.move;

import cat.yoink.xanax.internal.module.ModuleCategory;
import cat.yoink.xanax.internal.module.main.ModuleData;
import cat.yoink.xanax.internal.module.state.StateModule;
import cat.yoink.xanax.internal.setting.annotation.Setting;
import cat.yoink.xanax.internal.setting.annotation.types.Number;
import cat.yoink.xanax.internal.util.WorldUtil;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

/**
 * @author yoink
 */
@ModuleData(name = "Timer", aliases = {"Timer", "Time"}, category = ModuleCategory.MOVEMENT, description = "Change the client side tick rate")
public class Timer extends StateModule
{
    @Setting(name = "TPS", description = "Tick rate", number = @Number(min = 0.1, max = 150, increment = 0.1))
    public double tps = 2;

    @SubscribeEvent
    public void onTickClientTick(TickEvent.ClientTickEvent event)
    {
        if (isSafe()) WorldUtil.setTPS(tps);
    }

    @Override
    public void onDisable()
    {
        WorldUtil.setTPS(20);
    }
}
