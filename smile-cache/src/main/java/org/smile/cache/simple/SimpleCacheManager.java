package org.smile.cache.simple;

import org.smile.cache.AbstractCacheManager;
import org.smile.cache.Cache;
import org.smile.commons.SmileRunException;

public class SimpleCacheManager extends AbstractCacheManager{
	
	private Class<? extends AbstractCacheMap> cacheType=LFUCache.class;
	
	private int cacheSize=Short.MAX_VALUE;
	
	private long timeount=60;
	
	@Override
	public Cache getCache(String cacheName) {
		return getCache(cacheName, true);
	}

	public void setCacheType(Class<? extends AbstractCacheMap> cacheType) {
		this.cacheType = cacheType;
	}

	public void setCacheSize(int cacheSize) {
		this.cacheSize = cacheSize;
	}

	public void setTimeount(long timeount) {
		this.timeount = timeount;
	}

	

	@Override
	public Cache getCache(String cacheName, boolean ifnullInit) {
		Cache cache=this.cacheMap.get(cacheName);
		if(cache==null&&ifnullInit){
			synchronized (this) {
				cache=this.cacheMap.get(cacheName);
				if(cache==null){
					AbstractCacheMap newCache;
					try {
						newCache = cacheType.getConstructor(int.class,int.class).newInstance(cacheSize,timeount*1000);
					} catch (Exception e) {
						throw new SmileRunException("can not new instance cache "+cacheType, e);
					}
					newCache.setName(cacheName);
					addCache(cache);
				}
				return cache;
			}
		}
		return cache;
	}
	
}
