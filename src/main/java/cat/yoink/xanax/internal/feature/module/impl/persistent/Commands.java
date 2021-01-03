package cat.yoink.xanax.internal.feature.module.impl.persistent;

import cat.yoink.xanax.internal.feature.command.CommandManager;
import cat.yoink.xanax.internal.feature.module.ModuleCategory;
import cat.yoink.xanax.internal.feature.module.main.BasicModule;
import cat.yoink.xanax.internal.feature.module.main.ModuleData;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author yoink
 */
@ModuleData(
        name = "Commands",
        aliases = {"Commands", "Command"},
        category = ModuleCategory.CLIENT,
        hidden = true,
        description = "Allows commands to work"
)
public final class Commands extends BasicModule
{
    @SubscribeEvent
    public void onClientChat(ClientChatEvent event)
    {
        CommandManager.INSTANCE.parseCommand(event);
    }
}
