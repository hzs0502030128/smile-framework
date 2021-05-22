package org.smile.cache.plugin;

import java.util.List;

import org.smile.cache.Cache;
import org.smile.cache.plugin.ann.CacheWipe;
import org.smile.cache.plugin.ann.Cacheable;
import org.smile.plugin.Invocation;

public abstract class AbstractCacheScheme implements CacheScheme {
	/**
	 * 方案名称
	 */
	protected String name;
	/**
	 * 方案使用的缓存
	 */
	protected Cache cache;
	/**缓存键生成策略*/
	protected KeyGenerator keyGenerator;
	/**对缓存操作*/
	protected CacheKeyOperator operator;
	
	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public KeyGenerator getKeyGenerator() {
		return keyGenerator;
	}

	public void setKeyGenerator(KeyGenerator keyGenerator) {
		this.keyGenerator = keyGenerator;
	}

	public CacheKeyOperator getOperator() {
		return operator;
	}

	public void setOperator(CacheKeyOperator operator) {
		this.operator = operator;
	}

	/**
	 * 构建缓存的键
	 * @param invocation
	 * @param cacheWrite 
	 * @return
	 */
	@Override
	public CacheKey generateWrite(Invocation invocation, Cacheable cacheWrite) {
		return this.keyGenerator.generateWrite(invocation, cacheWrite);
	}

	/**
	 * 读取缓存
	 * @param cacheKey
	 * @return
	 */
	@Override
	public Object read(CacheKey cacheKey) {
		return this.operator.get(getCache(), cacheKey);
	}

	/**
	 * 写入缓存  不指定缓存时长 
	 * @param cacheKey
	 * @param result
	 */
	@Override
	public void write(CacheKey cacheKey, Object result) {
		this.operator.add(getCache(), cacheKey, result);
	}

	/**
	 * 写缓存
	 * @param cacheKey 
	 * @param result 需要缓存的执行结果
	 * @param timeout 缓存的时长 单位 毫秒
	 */
	@Override
	public void write(CacheKey cacheKey, Object result, long timeout) {
		this.operator.add(getCache(), cacheKey, result, timeout);
	}
	/***
	 * 移除缓存
	 * @param wipe 
	 * @param invocation
	 */
	@Override
	public void remove(CacheWipe wipe ,Invocation invocation){
		if(wipe.loop()){
			List<CacheKey> cacheKeys=this.keyGenerator.generateLoopWipe(invocation,wipe);
			this.operator.remove(getCache(),cacheKeys);
		}else{
			CacheKey cacheKey=this.keyGenerator.generateWipe(invocation,wipe);
			this.operator.remove(getCache(),cacheKey,wipe);
		}
	}
	@Override
	public Cache getCache() {
		return cache;
	}

	@Override
	public void setCache(Cache cache) {
		this.cache = cache;
	}
	
}
