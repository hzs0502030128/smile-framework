package org.smile.cache.plugin.impl;

import org.smile.cache.CacheNameAware;
import org.smile.cache.CacheWrap;
import org.smile.cache.ecache.BaseEcache;
import org.smile.cache.plugin.CacheScheme;
import org.smile.cache.plugin.DefaultCacheScheme;
/**
 * 预设的一种ehcache方案
 * 支持忽略参数移除，但此实现执行率可随缓存数据条数增多下降快
 * 建议使用 {@link LayerLFUCacheScheme}
 * @author 胡真山
 *
 */
public class LayerEhCacheScheme extends DefaultCacheScheme implements CacheNameAware{
	
	public LayerEhCacheScheme(){
		super(new LayerEhcache(), new LayerKeyGenerator(), new LayerEhcacheKeyOperator());
	}
	
	/**
	 * 设置使用的ehcach的名称
	 * @param name
	 */
	@Override
	public void setCacheWrap(CacheWrap cacheWrap){
		((BaseEcache)this.cache).setCacheWrap(cacheWrap);
	}

	@Override
	public void setCacheName(String name) {
		((BaseEcache)this.cache).setCacheName(name);
	}
	
}
