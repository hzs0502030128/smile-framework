package org.smile.commons.ann;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.smile.commons.Strings;

/**
 * @author 胡真山
 * 注解式 参考
 */
@Target(ElementType.FIELD)   
@Retention(RetentionPolicy.RUNTIME) 
public @interface Resource {
	/***
	 * service名称
	 * @return
	 */
	public String name() default Strings.BLANK;
	/**
	 * 类型
	 * @return
	 */
	public Class type() default NULL.class;
}
