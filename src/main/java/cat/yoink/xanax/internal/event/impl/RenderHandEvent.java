package cat.yoink.xanax.internal.event.impl;

import cat.yoink.xanax.internal.event.CustomEvent;
import net.minecraft.util.EnumHandSide;

public final class RenderHandEvent extends CustomEvent<RenderHandEvent>
{
    private final EnumHandSide hand;

    public RenderHandEvent(EnumHandSide hand)
    {
        this.hand = hand;
    }

    public EnumHandSide getHand()
    {
        return hand;
    }
}
