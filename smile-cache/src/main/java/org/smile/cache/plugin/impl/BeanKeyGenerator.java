package org.smile.cache.plugin.impl;

import java.util.List;

import org.smile.cache.plugin.CacheKey;
import org.smile.cache.plugin.KeyGenerator;
import org.smile.cache.plugin.ann.CacheWipe;
import org.smile.cache.plugin.ann.Cacheable;
import org.smile.collection.CollectionUtils;
import org.smile.plugin.Invocation;
/**
 * 这是一个简单的实现  
 * 不支持循环移除
 * 不支持自定义字段
 * 不支持忽略参数值移除
 * @author 胡真山
 *
 */
public class BeanKeyGenerator implements KeyGenerator {
	
	@Override
	public CacheKey generateWipe(Invocation invocation,CacheWipe wipe) {
		return new BeanCacheKey(invocation.getTarget(), invocation.getMethod(), invocation.getArgs());
	}
	
	@Override
	public List<CacheKey> generateLoopWipe(Invocation invocation,CacheWipe wipe) {
		return CollectionUtils.linkedList(generateWipe(invocation, wipe));
	}

	@Override
	public CacheKey generateWrite(Invocation invocation, Cacheable write) {
		return new BeanCacheKey(invocation.getTarget(), invocation.getMethod(), invocation.getArgs());
	}
}