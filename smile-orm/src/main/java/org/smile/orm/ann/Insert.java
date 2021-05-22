package org.smile.orm.ann;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.smile.annotation.AliasFor;
import org.smile.commons.Strings;
import org.smile.db.Db;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Sql(type=Db.INSERT)
public @interface Insert {
	/**
	 * sql 
	 * @return
	 */
	@AliasFor(annotation = Sql.class) String sql() default Strings.BLANK;
	@AliasFor(annotation = Sql.class) String sqlType() default Strings.BLANK;
	/**模板类型*/
	@AliasFor(annotation = Sql.class) String template() default Strings.BLANK;
	/**包含文件名称*/
	@AliasFor(annotation = Sql.class) String include() default Strings.BLANK;
}
