package org.smile.orm.ann;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.smile.annotation.AliasFor;
import org.smile.commons.Strings;
/**
 * 注解一个批量操作方法
 * @author 胡真山
 */
@Target(ElementType.METHOD)   
@Retention(RetentionPolicy.RUNTIME) 
@Sql(batch = true,type=Strings.BLANK)
public @interface Batch{
	/**
	 * sql 语句  如果方法参数配置了table注解的类 ,可以为空 程序会自动生成sql
	 * @return 
	 */
	@AliasFor(annotation = Sql.class) String sql() default Strings.BLANK;
	/**操作的类型    更新(update)  删除(delete)  插入 (insert)
	 * 只有在没有指定sql语句时，需要自动生成语句时候需要用类型
	 * 根据类型生成 相应的插入 删除 更新语句
	 * */
	@AliasFor(annotation = Sql.class) String type() default Strings.BLANK;
	/**sql类型*/
	@AliasFor(annotation = Sql.class) String sqlType() default Strings.BLANK;
	/**模板类型*/
	@AliasFor(annotation = Sql.class) String template() default Strings.BLANK;
	/**包含文件名称*/
	@AliasFor(annotation = Sql.class) String include() default Strings.BLANK;
}
