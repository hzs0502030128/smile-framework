package org.smile.json.format;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.smile.commons.Strings;

@Target({ElementType.FIELD,ElementType.METHOD})   
@Retention(RetentionPolicy.RUNTIME) 
public @interface JsonIgnore {
	/**忽略的条件 为空时为所有的都忽略*/
	public String value() default Strings.BLANK;
}
