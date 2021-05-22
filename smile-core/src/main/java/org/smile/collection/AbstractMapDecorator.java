package org.smile.collection;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
/**
 * 对map进行一个装饰
 * @author 胡真山
 * @date 2019年6月7日
 * 
 * @param <K>
 * @param <V>
 */
public abstract class AbstractMapDecorator<K,V> implements Map<K,V> {
	/**实现的被包装的map*/
	protected Map<K,V> realMap;

	protected AbstractMapDecorator(Map<K,V> map) {
		if (map == null) {
			throw new IllegalArgumentException("Map must not be null");
		} else {
			this.realMap = map;
		}
	}

	@Override
	public void clear() {
		this.realMap.clear();
	}
	@Override
	public boolean containsKey(Object key) {
		return this.realMap.containsKey(key);
	}
	@Override
	public boolean containsValue(Object value) {
		return this.realMap.containsValue(value);
	}
	@Override
	public Set<Map.Entry<K, V>> entrySet() {
		return this.realMap.entrySet();
	}

	@Override
	public V get(Object key) {
		return this.realMap.get(key);
	}
	@Override
	public boolean isEmpty() {
		return this.realMap.isEmpty();
	}
	@Override
	public Set<K> keySet() {
		return this.realMap.keySet();
	}
	@Override
	public V put(K key, V value) {
		return this.realMap.put(key, value);
	}
	@Override
	public void putAll(Map<? extends K, ? extends V> mapToCopy) {
		this.realMap.putAll(mapToCopy);
	}
	@Override
	public V remove(Object key) {
		return this.realMap.remove(key);
	}
	@Override
	public int size() {
		return this.realMap.size();
	}
	@Override
	public Collection<V> values() {
		return this.realMap.values();
	}
	@Override
	public boolean equals(Object object) {
		return object == this ? true : this.realMap.equals(object);
	}
	@Override
	public int hashCode() {
		return this.realMap.hashCode();
	}
	@Override
	public String toString() {
		return this.realMap.toString();
	}
}
