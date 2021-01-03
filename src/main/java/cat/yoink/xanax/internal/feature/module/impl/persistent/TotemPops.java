package cat.yoink.xanax.internal.feature.module.impl.persistent;

import cat.yoink.xanax.internal.event.impl.PacketEvent;
import cat.yoink.xanax.internal.event.impl.PopEvent;
import cat.yoink.xanax.internal.feature.module.ModuleCategory;
import cat.yoink.xanax.internal.feature.module.main.BasicModule;
import cat.yoink.xanax.internal.feature.module.main.ModuleData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@ModuleData(
        name = "TotemPops",
        aliases = {"TotemPop", "PopManager"},
        category = ModuleCategory.CLIENT,
        description = "List for the totem pops",
        hidden = true
)
public final class TotemPops extends BasicModule
{
    @SubscribeEvent
    public void onPacket(PacketEvent event)
    {
        if (isSafe() && event.getType().equals(PacketEvent.Type.INCOMING) && event.getPacket() instanceof SPacketEntityStatus)
        {
            SPacketEntityStatus packet = (SPacketEntityStatus) event.getPacket();
            if (packet.getOpCode() == 35) new PopEvent((EntityPlayer) packet.getEntity(mc.world)).dispatch();
        }
    }
}
