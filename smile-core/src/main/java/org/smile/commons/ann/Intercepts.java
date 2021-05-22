package org.smile.commons.ann;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 胡真山
 * 用于拦截器注解多个
 */
@Target(ElementType.TYPE)   
@Retention(RetentionPolicy.RUNTIME) 
public @interface Intercepts {
	/**多个拦截器*/
	public Intercept[] values();
}
