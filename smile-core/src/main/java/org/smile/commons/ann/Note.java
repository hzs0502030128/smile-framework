package org.smile.commons.ann;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.smile.commons.Strings;

@Target({ElementType.FIELD,ElementType.METHOD,ElementType.TYPE})   
@Retention(RetentionPolicy.RUNTIME) 
/**
 * @author 胡真山
 * @Date 2016年3月3日
 */
public @interface Note
{
	/**注释*/
	public String value() default Strings.BLANK;
}
