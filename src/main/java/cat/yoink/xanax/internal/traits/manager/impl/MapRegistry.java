package cat.yoink.xanax.internal.traits.manager.impl;

import cat.yoink.xanax.internal.traits.manager.Manager;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yoink
 */
public abstract class MapRegistry<K, V> implements Manager<Map<K, V>>
{
    private final Map<K, V> registry = new HashMap<>();

    public final void register(K k, V v)
    {
        registry.put(k, v);
    }

    public final void unregister(K k)
    {
        registry.remove(k);
    }

    public final boolean containsKey(K k)
    {
        return registry.containsKey(k);
    }

    public final boolean containsValue(V v)
    {
        return registry.containsValue(v);
    }

    public final V getValue(K k)
    {
        return registry.get(k);
    }

    public final K getKey(V v)
    {
        return registry.entrySet().stream().filter(entry -> v.equals(entry.getValue())).map(Map.Entry::getKey).findAny().orElse(null);
    }

    @Override
    public final Map<K, V> getRegistry()
    {
        return registry;
    }
}
