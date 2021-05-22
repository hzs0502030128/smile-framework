package org.smile.orm.ann;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.smile.annotation.AliasFor;
import org.smile.commons.Strings;
import org.smile.db.Db;
/**
 * 用于注解一个删除操作方法
 * @author 胡真山
 *
 */
@Target(ElementType.METHOD)   
@Retention(RetentionPolicy.RUNTIME) 
@Sql(type=Db.DELETE)
public @interface Delete {
	/**
	 * sql 
	 * @return
	 */
	@AliasFor(annotation = Sql.class) String sql()  default Strings.BLANK;
	/**语句类型*/
	@AliasFor(annotation = Sql.class) String sqlType() default Strings.BLANK;
	/**模板类型*/
	@AliasFor(annotation = Sql.class) String template() default Strings.BLANK;
	/**包含文件名称*/
	@AliasFor(annotation = Sql.class) String include() default Strings.BLANK;
}
