package cat.yoink.xanax.internal.feature;

import cat.yoink.xanax.internal.traits.interfaces.Minecraft;
import cat.yoink.xanax.internal.traits.manager.Manager;

/**
 * @author yoink
 */
public interface Feature extends Minecraft
{
    Manager<?> getManager();
}
