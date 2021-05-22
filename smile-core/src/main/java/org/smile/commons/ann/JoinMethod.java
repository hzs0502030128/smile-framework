package org.smile.commons.ann;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.smile.plugin.MethodInterceptor;
/**
 * 注解注入一个拦截器
 * @author 胡真山
 *
 */
@Target(ElementType.METHOD)   
@Retention(RetentionPolicy.RUNTIME) 
public @interface JoinMethod {
	/**要注入的拦截器*/
	public Class<? extends MethodInterceptor> type();
}
