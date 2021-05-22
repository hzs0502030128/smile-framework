package org.smile.cache.plugin.impl;

import org.smile.cache.CacheManager;
import org.smile.cache.CacheNameAware;
import org.smile.cache.plugin.DefaultCacheScheme;

public class CacheManagerScheme extends DefaultCacheScheme implements CacheNameAware{
	/**产生cache的管理*/
	protected CacheManager cacheManager;
	
	public CacheManagerScheme(){
		super();
	}
	
	public CacheManagerScheme(CacheManager cacheManager){
		this.cacheManager=cacheManager;
	}
	
	@Override
	public void setCacheName(String name) {
		if(cacheManager==null){
			throw new IllegalArgumentException("please setCacheManager before setCacheName");
		}
		this.cache=cacheManager.getCache(name);
	}
	
	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}
	
}
