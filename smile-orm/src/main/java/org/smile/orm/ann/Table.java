package org.smile.orm.ann;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.smile.annotation.AliasFor;
import org.smile.commons.Strings;
/**
 * 表映射注解
 * @author 胡真山
 */
@Target(ElementType.TYPE)   
@Retention(RetentionPolicy.RUNTIME)
@Orm(isTable = true)
public @interface Table
{
	@AliasFor(annotation = Orm.class) String name() default Strings.BLANK;
}
