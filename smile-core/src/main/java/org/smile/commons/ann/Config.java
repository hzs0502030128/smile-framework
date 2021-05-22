package org.smile.commons.ann;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.smile.commons.Strings;

@Target(ElementType.TYPE)   
@Retention(RetentionPolicy.RUNTIME)
@Bean
public @interface Config {
	/***
	 * 	配置文件路径
	 * @return
	 */
	public String value() default Strings.BLANK;
	
	public String prefix() default Strings.BLANK;
	
}
