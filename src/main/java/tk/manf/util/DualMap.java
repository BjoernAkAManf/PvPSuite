package tk.manf.util;

import java.util.Map;

public interface DualMap<K, V> extends Map<K, V> {
    public Map<V, K> getValueMap();
    public K removeValue(V value);
}