package cat.yoink.xanax.internal.traits.manager;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yoink
 */
public abstract class MapRegistry<K, V>
{
    private final Map<K, V> registry = new HashMap<>();

    public void add(K k, V v)
    {
        registry.put(k, v);
    }

    public void remove(K k)
    {
        registry.remove(k);
    }

    public Map<K, V> getRegistry()
    {
        return registry;
    }
}
