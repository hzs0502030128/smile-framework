package org.smile.commons.ann;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE,ElementType.METHOD})   
@Retention(RetentionPolicy.RUNTIME) 
public @interface Sington {
	/**
	 *	配置是否单例
	 * @return
	 */
	public boolean value() default true;
}
