/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tk.manf.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import lombok.Getter;

public class DualHashMap<K, V> implements DualMap<K, V> {
    private HashMap<K, V> keyMap;
    @Getter
    private HashMap<V, K> valueMap;

    public DualHashMap() {
        this(DEFAULT_INITIAL_CAPACITY);
    }

    public DualHashMap(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    public DualHashMap(int initialCapacity, float loadFactor) {
        keyMap = new HashMap<K, V>(initialCapacity, loadFactor);
        valueMap = new HashMap<V, K>(initialCapacity, loadFactor);
    }

    public int size() {
        return keyMap.size();
    }

    public boolean isEmpty() {
        return keyMap.isEmpty();
    }

    public boolean containsKey(Object key) {
        return keyMap.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return keyMap.containsValue(value);
    }

    public V get(Object key) {
        return keyMap.get(key);
    }

    public V put(K key, V value) {
        valueMap.put(value, key);
        return keyMap.put(key, value);
    }

    public V remove(Object key) { 
        V a = keyMap.remove((K) key);
        valueMap.remove(a);
        return a;
    }

    public K removeValue(Object value) {
        K a = valueMap.remove((V) value);
        keyMap.remove(a);
        return a;
    }

    public void putAll(Map<? extends K, ? extends V> tmp) {
        for (K key : tmp.keySet()) {
            valueMap.put(tmp.get(key), key);
        }
        keyMap.putAll(tmp);
    }

    public void clear() {
        valueMap.clear();
        keyMap.clear();
    }

    public Set<K> keySet() {
        return keyMap.keySet();
    }

    public Collection<V> values() {
        return keyMap.values();
    }

    public Set<Entry<K, V>> entrySet() {
        return keyMap.entrySet();
    }
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
}
