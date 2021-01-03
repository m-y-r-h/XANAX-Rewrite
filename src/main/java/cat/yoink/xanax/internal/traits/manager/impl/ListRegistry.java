package cat.yoink.xanax.internal.traits.manager.impl;

import cat.yoink.xanax.internal.traits.manager.Manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author yoink
 */
public abstract class ListRegistry<E> implements Manager<List<E>>
{
    private final List<E> registry = new ArrayList<>();

    public final void register(E e)
    {
        registry.add(e);
    }

    @SafeVarargs
    public final void addAll(E... e)
    {
        registry.addAll(Arrays.asList(e));
    }

    public final void unregister(E e)
    {
        registry.remove(e);
    }

    @SafeVarargs
    public final void removeAll(E... e)
    {
        registry.removeAll(Arrays.asList(e));
    }

    public final E get(int i)
    {
        return registry.get(i);
    }

    @Override
    public final List<E> getRegistry()
    {
        return registry;
    }
}
