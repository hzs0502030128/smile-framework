package org.smile.commons.ann;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.smile.commons.Strings;

@Target({ElementType.TYPE,ElementType.METHOD})   
@Retention(RetentionPolicy.RUNTIME) 
public @interface Bean {
	/***
	 * 	配置bean的名称
	 * @return
	 */
	public String value() default Strings.BLANK;
}
