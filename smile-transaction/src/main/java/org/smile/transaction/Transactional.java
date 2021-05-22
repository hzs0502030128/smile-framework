package org.smile.transaction;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.smile.annotation.AliasFor;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.TYPE})
public @interface Transactional {
	@AliasFor("propagation")
	public Propagation value() default Propagation.REQUIRED;
	@AliasFor("value")
	public Propagation propagation() default Propagation.REQUIRED;
}
