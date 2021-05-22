package org.smile.cache;

import org.smile.commons.Strings;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public abstract class AbstractCacheManager implements CacheManager{
	/***缓存的cache*/
	protected final ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap<String, Cache>();
	/**缓存下名称 保留添加的顺序*/
	private Set<String> cacheNames = new LinkedHashSet<String>();
	/**名称*/
	protected String name= Strings.DOLLAR;
	
	@Override
	public final synchronized void addCache(Cache cache) {
		if(cacheMap.containsKey(cache.getName())){
			return ;
		}
		this.cacheMap.put(cache.getName(), decorateCache(cache));
		this.cacheNames.add(cache.getName());
	}
	/***
	 * 包装缓存
	 * @param cache
	 * @return
	 */
	protected Cache decorateCache(Cache cache) {
		return cache;
	}

	@Override
	public Collection<String> getCacheNames() {
		return Collections.unmodifiableSet(this.cacheNames);
	}

	@Override
	public boolean existsCache(String cacheName) {
		return cacheMap.containsKey(cacheName);
	}
	
	@Override
	public Cache getCache(String name){
		return getCache(name, true);
	}

	@Override
	public String getName() {
		return this.name;
	}
}
