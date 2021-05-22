package org.smile.cache;

public class CacheWrapBean implements CacheWrap{
	
	protected CacheManager cacheManager;
	
	protected String name;
	
	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public Cache getCache() {
		return cacheManager.getCache(name);
	}
}
