package org.smile.commons.ann;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.smile.commons.Strings;

/**
 * @author 胡真山
 * 用于拦截器注解
 */
@Target(ElementType.TYPE)   
@Retention(RetentionPolicy.RUNTIME) 
public @interface Intercept {
	/**指定要拦截的接口类*/
	public Class type() default NULL.class;
	/**指定要拦截的方法 可以是正则表达式*/
	public String method() default Strings.BLANK;
	/**拦截方法的参数类型 在方法重载时,只拦截部分方法名*/
	public Class[] args() default {NULL.class};
}
