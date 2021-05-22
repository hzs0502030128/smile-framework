package org.smile.cache.plugin.aspectj;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.smile.cache.plugin.BaseCachePluginHandler;
import org.smile.cache.plugin.ann.CacheWipe;
import org.smile.cache.plugin.ann.Cacheable;
import org.smile.cache.plugin.util.CachePluginUtils;
import org.smile.collection.ArrayUtils;
import org.smile.plugin.aspectj.JoinPointInvocation;
/**
 * aspectj 注入方式实现 
 * 
 * 例 spring中配置 
 * <bean id="aspectjCacheHandler" class="org.smile.cache.plugin.aspectj.AspectjCacheHandler">
 *		<property name="cache">
 *			<bean class="org.smile.cache.plugin.impl.LayerLFUCache"></bean>
 *		</property>
 *		<property name="keyGenerator">
 *			<bean class="org.smile.cache.plugin.impl.LayerKeyGenerator"></bean>
 *		</property>
 *		<property name="operator">
 *			<bean class="org.smile.cache.plugin.impl.LayerKeyOperator"></bean>
 *		</property>
 *	</bean>
 *	
 *	<aop:aspect ref="aspectjCacheHandler">
 *        	<aop:around method="processCache" pointcut-ref="cachePluginPointCuts" />
 *  </aop:aspect>
 * 
 * @author 胡真山
 *
 */
public class AspectjCacheHandler extends BaseCachePluginHandler{
	/**
	 * 处理缓存
	 * @param pjp
	 * @return
	 * @throws Throwable
	 */
	public Object processCache(ProceedingJoinPoint pjp) throws Throwable {
		// AOP获取方法执行信息
		Signature signature = pjp.getSignature();
		MethodSignature methodSignature = (MethodSignature) signature;
		Method method = methodSignature.getMethod();
		if(method!=null){
			// 查缓存逻辑
			Cacheable writeHandle = method.getAnnotation(Cacheable.class);
			if (writeHandle != null) {
				return doCacheable(writeHandle,new JoinPointInvocation(pjp));
			}
			// 清缓存逻辑
			CacheWipe[] handles=CachePluginUtils.getCacheWipes(method);
			if (ArrayUtils.notEmpty(handles)) {
				return doCacheWipe(handles,new JoinPointInvocation(pjp));
			}
		}
		return pjp.proceed();
	}
}
