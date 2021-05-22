package org.smile.cache.plugin;

import java.lang.reflect.Method;

import org.smile.cache.plugin.ann.CacheWipe;
import org.smile.cache.plugin.ann.Cacheable;
import org.smile.cache.plugin.util.CachePluginUtils;
import org.smile.collection.ArrayUtils;
import org.smile.commons.ann.Intercept;
import org.smile.plugin.Interceptor;
import org.smile.plugin.Invocation;
import org.smile.plugin.Plugin;
/**
 * 使用代理对要拦截缓存的对象进行代理
 * @author 胡真山
 *
 */
@Intercept(method="*")
public class CachePluginInterceptor extends BaseCachePluginHandler implements Interceptor {

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		Method method=invocation.getMethod();
		
		Cacheable writeHandle = method.getAnnotation(Cacheable.class);
		if (writeHandle != null) {
			return doCacheable(writeHandle,invocation);
		}
		
		CacheWipe[] cacheWips=CachePluginUtils.getCacheWipes(method);
		if(ArrayUtils.notEmpty(cacheWips)){
			return doCacheWipe(cacheWips, invocation);
		}
		
		return invocation.proceed();
	}

}
