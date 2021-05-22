package org.smile.orm.ann;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.smile.annotation.AliasFor;
import org.smile.commons.Strings;

@Target({ElementType.FIELD,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME) 
@Property
public @interface Column {
	/**列名*/
	@AliasFor(value="column", annotation = Property.class) String value() default Strings.BLANK;
	/**是否是自增*/
	@AliasFor(annotation = Property.class) boolean autoincrement() default false;
	/**是否存贮数据库*/
	@AliasFor(annotation = Property.class) boolean store() default true;
}
