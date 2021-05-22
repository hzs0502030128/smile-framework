package org.smile.ann;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.smile.annotation.AliasFor;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.TYPE})
@Ann3(name = "name123",value = "value123")
public @interface Ann2 {
	@AliasFor(annotation = Ann3.class)
	public String text() default "";
	@AliasFor(annotation = Ann3.class)
	public String name() default "";
}
