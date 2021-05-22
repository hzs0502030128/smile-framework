package org.smile.cache.plugin;

import org.smile.cache.Cache;
import org.smile.cache.plugin.ann.CacheWipe;
import org.smile.cache.plugin.ann.Cacheable;
import org.smile.plugin.Invocation;

public interface CacheScheme {
	/**方案名称*/
	public abstract String getName();
	
	public void setCache(Cache cache);
	
	public Cache getCache();
	/**
	 * 构建缓存的键
	 * @param invocation
	 * @param cacheWrite 
	 * @return
	 */
	public abstract CacheKey generateWrite(Invocation invocation, Cacheable cacheWrite);

	/**
	 * 读取缓存
	 * @param cacheKey
	 * @return
	 */
	public abstract Object read(CacheKey cacheKey);

	/**
	 * 写入缓存  不指定缓存时长 
	 * @param cacheKey
	 * @param result
	 */
	public abstract void write(CacheKey cacheKey, Object result);

	/**
	 * 写缓存
	 * @param cacheKey 
	 * @param result 需要缓存的执行结果
	 * @param timeout 缓存的时长 单位 毫秒
	 */
	public abstract void write(CacheKey cacheKey, Object result, long timeout);

	/***
	 * 移除缓存
	 * @param wipe 
	 * @param invocation
	 */
	public abstract void remove(CacheWipe wipe, Invocation invocation);
}
