package org.smile.cache;

public interface CacheModel<K,V> {
	/**
	 * 缓存的对象
	 * @return
	 */
	public V getObject();
	/**
	 * 缓存的key
	 * @return
	 */
	public K getKey();
	
}
