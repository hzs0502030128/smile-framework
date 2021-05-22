package org.smile.orm.ann;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.smile.annotation.AliasFor;
import org.smile.commons.Strings;
import org.smile.commons.ann.NULL;

@Target(ElementType.FIELD)   
@Retention(RetentionPolicy.RUNTIME)
@Property(primaryKey = true,store=true)
public @interface Id 
{
	/**数据库对应列名*/
	@AliasFor(annotation = Property.class) String column() default Strings.BLANK;
	/**是否是uuid*/
	@AliasFor(annotation = Property.class) boolean uuid() default false;
	/**是否是自增*/
	@AliasFor(annotation = Property.class) boolean autoincrement() default false;
	/**ID生成器*/
	@AliasFor(annotation = Property.class) Class idGenerator() default NULL.class;
}
