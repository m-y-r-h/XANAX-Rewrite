// TODO: 12/26/2020
package cat.yoink.xanax.internal.friend;

import java.util.UUID;

public final class Friend
{
    private final UUID uuid;
    private final String name;

    public Friend(UUID uuid, String name)
    {
        this.uuid = uuid;
        this.name = name;
    }

    public UUID getUuid()
    {
        return uuid;
    }

    public String getName()
    {
        return name;
    }
}
