package org.smile.ioc.ann;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.smile.annotation.AliasFor;
import org.smile.commons.Strings;
import org.smile.commons.ann.Bean;
@Target({ElementType.TYPE,ElementType.METHOD})   
@Retention(RetentionPolicy.RUNTIME) 
@Bean
public @interface Configuration {
	@AliasFor(annotation = Bean.class)
	public String value() default Strings.BLANK;
}
