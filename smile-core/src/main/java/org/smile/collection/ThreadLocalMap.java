package org.smile.collection;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ThreadLocalMap<K,V> extends ThreadLocal<Map<K,V>> implements Map<K,V>{
	
	@Override
	protected Map<K,V> initialValue() {
		return new HashMap<K,V>();
	}

	@Override
	public int size() {
		return get().size();
	}

	@Override
	public boolean isEmpty() {
		return get().isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return get().containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return get().containsValue(value);
	}

	@Override
	public V get(Object key) {
		return get().get(key);
	}

	@Override
	public V put(K key, V value) {
		return get().put(key, value);
	}

	@Override
	public V remove(Object key) {
		return get().remove(key);
	}

	@Override
	public void clear() {
		get().clear();
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		get().putAll(m);
	}

	@Override
	public Set<K> keySet() {
		return get().keySet();
	}

	@Override
	public Collection<V> values() {
		return get().values();
	}

	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		return get().entrySet();
	}

	@Override
	public String toString() {
		return get().toString();
	}
}
