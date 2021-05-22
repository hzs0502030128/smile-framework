package org.smile.cache;

import java.util.Collection;
import java.util.Iterator;

/**
 * Cache interface.
 */
public interface Cache<K, V> {

	/**
	 * Returns cache size or <code>0</code> if there is no size limit.
	 */
	long getCacheSize();

	/**
	 * Returns default timeout or <code>0</code> if it is not set.
	 */
	long getCacheTimeout();

	/**
	 * Adds an object to the cache with default timeout.
	 * @see Cache#put(Object, Object, long)
	 */
	void put(K key, V object);

	/**
	 *Adds an object to the cache with specified timeout after which it becomes expired.
	 * If cache is full, {@link #prune()} is invoked to make room for new object. 
	 * @param key
	 * @param object
	 * @param timeout 过期ms
	 */
	void put(K key, V object, long timeout);
	/**
	 * 缓存一个需要缓存的对象
	 * @param model
	 * @param timeout
	 */
	void put(CacheModel<K, V> model,long timeout);

	/**
	 * Retrieves an object from the cache. Returns <code>null</code> if object
	 * is not longer in cache or if it is expired.
	 */
	V get(K key);

	/**
	 * Returns iterator over non-expired values.
	 */
	Iterator<V> iterator();
	
	/**
	 * 迭代出所有的缓存元素
	 * @return
	 */
	Iterator<Element<K,V>> elements();

	/**
	 * 对过期的对象移除　
	 * 返回移除元素个数
	 * Prunes objects from cache and returns the number of removed objects.
	 * Used strategy depends on cache implementation.
	 */
	int prune();

	/**
	 * Returns <code>true</code> if max cache capacity has been reached
	 * only if cache is size limited.
	 */
	boolean isFull();

	/**
	 * Removes an object from the cache.
	 */
	void remove(K key);
	
	/**
	 * 一次删除多个缓存
	 * @param keys
	 */
	void remove(Collection<K> keys);

	/**
	 * Clears current cache.
	 */
	void clear();

	/**
	 * Returns current cache size.
	 */
	long size();

	/**
	 * Returns <code>true</code> if cache is empty.
	 */
	boolean isEmpty();
	/**
	 * 缓存的名称
	 * @return
	 */
	String getName();
	/**
	 * 所有的键
	 * @return
	 */
	Collection<K> keys();
	/***
	 * 没有过期的键
	 * @return
	 */
	Collection<K> noExpiredKeys();
	
}
