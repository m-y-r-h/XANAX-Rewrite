// TODO: 12/26/2020
package cat.yoink.xanax.internal.friend;

import cat.yoink.xanax.internal.traits.interfaces.Configurable;
import cat.yoink.xanax.internal.traits.manager.Register;

/**
 * @author yoink
 */
public final class FriendManager extends Register<Friend> implements Configurable
{
    public static final FriendManager INSTANCE = new FriendManager();

    private FriendManager()
    {

    }

    @Override
    public boolean save(String name)
    {
        return true;
    }

    @Override
    public boolean load(String name)
    {
        return true;
    }
}
