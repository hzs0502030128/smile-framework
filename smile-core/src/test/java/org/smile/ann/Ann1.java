package org.smile.ann;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.smile.annotation.AliasFor;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.TYPE})
@Ann2(text = "text123")
public @interface Ann1 {
	public String attr() default "";
	@AliasFor(annotation = Ann2.class)
	public String text() default "";
	@AliasFor(annotation = Ann2.class)
	public String name() default "";
}
