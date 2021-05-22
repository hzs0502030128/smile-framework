package org.smile.cache.plugin.impl;

import org.smile.cache.CacheNameAware;
import org.smile.cache.CacheWrap;
import org.smile.cache.ecache.BaseEcache;
import org.smile.cache.plugin.CacheScheme;
import org.smile.cache.plugin.DefaultCacheScheme;
/**
 * 使用ehcache实现的一种预设方案
 * @author 胡真山
 *
 */
public class EhcacheScheme extends DefaultCacheScheme implements CacheNameAware{

	public EhcacheScheme() {
		super(new BaseEcache(), new DefaultKeyGenerator(), new DefaultKeyOperator());
	}

	@Override
	public void setCacheWrap(CacheWrap cacheWrap) {
		((BaseEcache) this.cache).setCacheWrap(cacheWrap);
	}

	@Override
	public void setCacheName(String name) {
		((BaseEcache) this.cache).setCacheName(name);
	}
}
