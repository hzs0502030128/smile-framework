package org.smile.commons.ann;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.smile.commons.Strings;


/**
 * @author 胡真山
 * @Date 2016年3月3日
 */
@Target(ElementType.FIELD)   
@Retention(RetentionPolicy.RUNTIME) 
public @interface Attribute
{
	/**属性名*/
	public String value() default Strings.BLANK;
	/**必需的*/
	public boolean required() default false;
}
