package org.smile.cache.plugin;

import java.util.Collection;

import org.smile.cache.Cache;
import org.smile.cache.plugin.ann.CacheWipe;

public interface CacheKeyOperator<K,C extends Cache<K,Object>> {
	/**默认缓存时长*/
	public static final int DEFAULT_CACHE_TIME=24*3600;
	/**
	 * 移除一个key
	 * @param cache
	 * @param key
	 */
	public void remove(C cache,CacheKey<K> key,CacheWipe wipe);
	/**
	 * 添加缓存
	 * @param cache  
	 * @param key
	 * @param value
	 * @param timeout 缓存的时长 毫秒
	 */
	public void add(C cache,CacheKey<K> key,Object value,long timeout);
	/**
	 * 添加缓存
	 * @param cache  
	 * @param key
	 * @param value
	 * @param timeout 缓存的时长 毫秒
	 */
	public void add(C cache,CacheKey<K> key,Object value);
	/**
	 * 批量移除
	 * @param cache
	 * @param keys
	 */
	public void remove(C cache,Collection<CacheKey<K>> keys);
	/**
	 * 获取缓存内容对象
	 * @param cache 
	 * @param key
	 * @return
	 */
	public Object get(C cache,CacheKey<K> key);
}
