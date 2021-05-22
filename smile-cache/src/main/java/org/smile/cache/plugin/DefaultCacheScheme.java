package org.smile.cache.plugin;

import org.smile.cache.Cache;
import org.smile.cache.CacheWrap;
import org.smile.cache.CacheWrapSupport;
import org.smile.cache.plugin.impl.DefaultKeyGenerator;
import org.smile.cache.plugin.impl.DefaultKeyOperator;

/***
 * 缓存使用方案
 * @author 胡真山
 */
public class DefaultCacheScheme extends AbstractCacheScheme implements CacheWrapSupport{
	
	public DefaultCacheScheme(){
		//不初始化方案内容 方便在配置注入时使用
		this(null, new DefaultKeyGenerator(), new DefaultKeyOperator());
	}
	
	public DefaultCacheScheme(Cache cache,KeyGenerator keyGenerator,CacheKeyOperator operator){
		this.cache=cache;
		this.keyGenerator=keyGenerator;
		this.operator=operator;
	}

	@Override
	public void setCacheWrap(CacheWrap cacheWrap) {
		this.cache=cacheWrap.getCache();
	}

}
