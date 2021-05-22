package org.smile.validate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD,ElementType.TYPE}) 
@Retention(RetentionPolicy.RUNTIME) 
public @interface Validate {
	/**需验证的字段*/
	public Field[] fields();
}
