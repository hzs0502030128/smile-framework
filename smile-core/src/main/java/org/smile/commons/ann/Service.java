package org.smile.commons.ann;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.smile.annotation.AliasFor;
import org.smile.commons.Strings;

/**
 * @author 胡真山
 * 注解式Service
 */
@Target(ElementType.TYPE)   
@Retention(RetentionPolicy.RUNTIME)
@Bean
@Sington(true)
public @interface Service {
	/***
	 * service名称
	 * @return
	 */
	@AliasFor(value = "value",annotation = Bean.class)
	public String name() default Strings.BLANK;
	/***
	 * 是否单例
	 * @return
	 */
	@AliasFor(value = "value",annotation = Sington.class)
	public boolean single() default true;
}
