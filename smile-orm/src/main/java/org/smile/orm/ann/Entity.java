package org.smile.orm.ann;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.smile.annotation.AliasFor;
import org.smile.commons.Strings;

/**
 * 用来注解orm的maper
 * @author 胡真山
 *
 */
@Target(ElementType.TYPE)   
@Retention(RetentionPolicy.RUNTIME) 
@Orm(isTable = true)
public @interface Entity {
	/**
	 * 	表名
	 * @return 对应数据库表名
	 */
	@AliasFor(value="name",annotation = Orm.class) String table() default Strings.BLANK;
	
}
