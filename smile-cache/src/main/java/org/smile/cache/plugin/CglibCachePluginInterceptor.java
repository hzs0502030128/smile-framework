package org.smile.cache.plugin;

import org.smile.plugin.CglibPlugin;
/**
 * 使用cglib进行代理
 * @author 胡真山
 *
 */
public class CglibCachePluginInterceptor extends CachePluginInterceptor{
	@Override
	public Object plugin(Object target) {
		return CglibPlugin.wrap(target,this);
	}

}
