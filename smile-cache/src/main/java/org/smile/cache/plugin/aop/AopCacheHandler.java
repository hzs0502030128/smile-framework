package org.smile.cache.plugin.aop;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.smile.cache.plugin.BaseCachePluginHandler;
import org.smile.cache.plugin.ann.CacheWipe;
import org.smile.cache.plugin.ann.Cacheable;
import org.smile.cache.plugin.util.CachePluginUtils;
import org.smile.collection.ArrayUtils;
import org.smile.plugin.aop.AopInvocation;
/**
 * aop方式注入  
 * 
 * 例 spring 中配置
 * <bean id="cachePlugin" class="org.smile.cache.plugin.aop.AopCacheHandler">
		<property name="cache">
			<bean class="org.smile.cache.plugin.impl.LayerEhcache">
				<property name="name" value="smile-cache-plugin"></property>
			</bean>
		</property>
		<property name="keyGenerator">
			<bean class="org.smile.cache.plugin.impl.LayerKeyGenerator"></bean>
		</property>
		<property name="operator">
			<bean class="org.smile.cache.plugin.impl.LayerEhcacheKeyOperator"></bean>
		</property>
	</bean>
	
	...
	<aop:advisor advice-ref="cachePlugin" pointcut-ref="cachePluginPointCuts" />
 * 
 * @author 胡真山
 *
 */
public class AopCacheHandler extends BaseCachePluginHandler implements MethodInterceptor{

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Method method=invocation.getMethod();
		
		//处理读取缓存
		Cacheable writeHandle = method.getAnnotation(Cacheable.class);
		if (writeHandle != null) {
			return doCacheable(writeHandle,new AopInvocation(invocation));
		}
		
		//处理缓存擦除
		CacheWipe[] cacheWips=CachePluginUtils.getCacheWipes(method);
		if(ArrayUtils.notEmpty(cacheWips)){
			return doCacheWipe(cacheWips, new AopInvocation(invocation));
		}
		
		return invocation.proceed();
	}
	
}
