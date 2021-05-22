package org.smile.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.smile.commons.Strings;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AliasFor {
	/**可做为其它属性的别名*/
	public String value() default Strings.BLANK;
	/**对名对于的类型,如果是默认配置则是对于当前注解类型*/
	public Class<? extends Annotation> annotation() default Annotation.class;
}
