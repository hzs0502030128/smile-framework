package org.smile.cache.plugin;

import java.util.List;

import org.smile.cache.plugin.ann.CacheWipe;
import org.smile.cache.plugin.ann.Cacheable;
import org.smile.plugin.Invocation;

public interface KeyGenerator {
	/***
	 * 生成缓存键
	 * @param target 调用方法的对象
	 * @param method 方法
	 * @param args 方法的参数
	 * @return
	 */
	public abstract CacheKey generateWrite(Invocation invocation,Cacheable write);
	/***
	 * 生成缓存键
	 * @param target 调用方法的对象
	 * @param method 方法
	 * @param args 方法的参数
	 * @return
	 */
	public abstract CacheKey generateWipe(Invocation invocation,CacheWipe wipe);
	/**
	 * 生成循环删除时的key
	 * @param target
	 * @param method
	 * @param args
	 * @param configKey
	 * @param fieldNames
	 * @return
	 */
	public abstract List<CacheKey> generateLoopWipe(Invocation invocation,CacheWipe wipe);
}
