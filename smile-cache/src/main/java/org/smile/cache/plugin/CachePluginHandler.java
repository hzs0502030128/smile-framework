package org.smile.cache.plugin;

import org.smile.cache.plugin.ann.CacheWipe;
import org.smile.cache.plugin.ann.Cacheable;
import org.smile.plugin.Invocation;
/**
 * 对缓存插件进行处理的接口
 * @author 胡真山
 *
 */
public interface CachePluginHandler {
	/**
	 * 处理写入缓存方法 
	 * @param cacheable 注解写入缓存
	 * @param invocation 
	 * @return
	 * @throws Exception
	 */
	public Object doCacheable(Cacheable cacheable,Invocation invocation) throws Throwable;
	/**
	 * 处理移除缓存
	 * @param cacheWipes
	 * @param invocation
	 * @return
	 * @throws Exception
	 */
	public Object doCacheWipe(CacheWipe[] cacheWipes,Invocation invocation) throws Throwable;
}
