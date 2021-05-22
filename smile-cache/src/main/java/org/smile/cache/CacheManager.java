package org.smile.cache;

import java.util.Collection;

public interface CacheManager {
	/**
	 * 获取一个缓存
	 * @param cacheName
	 * @return
	 */
	public Cache getCache(String cacheName);
	/**
	 * 获取cacheName名称的缓存 如果不存在则初始化一个缓存
	 * @param cacheName
	 * @return
	 */
	public Cache getCache(String cacheName,boolean ifnullInit);
	/**
	 * 所有缓存的名称
	 * @return
	 */
	public Collection<String> getCacheNames();
	/**
	 * 是否存在缓存
	 * @param cacheName
	 * @return
	 */
	public boolean existsCache(String cacheName);
	/**
	 * 增加一个缓存
	 * @param cache
	 */
	public void addCache(Cache cache);

	/**
	 * 当前manager的名称
	 */
	public String getName();
}
