package org.smile.commons.ann;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.smile.commons.Strings;

@Target({ElementType.FIELD,ElementType.METHOD})   
@Retention(RetentionPolicy.RUNTIME) 
public @interface Value {
	/**
	 * 配置对应的key
	 * @return
	 */
	public String value() default Strings.BLANK;
}
