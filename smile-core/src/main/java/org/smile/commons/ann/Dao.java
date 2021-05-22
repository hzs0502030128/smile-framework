package org.smile.commons.ann;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.smile.commons.Strings;

/**
 * @author 胡真山
 * 注解式dao 用开标记Dao接口
 */
@Target(ElementType.TYPE)   
@Retention(RetentionPolicy.RUNTIME) 
public @interface Dao {
	/***
	 * dao名称
	 * @return
	 */
	public String name() default Strings.BLANK;
}
