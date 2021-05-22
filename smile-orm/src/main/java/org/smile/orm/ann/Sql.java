package org.smile.orm.ann;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.smile.commons.Strings;
@Target({ElementType.METHOD,ElementType.ANNOTATION_TYPE})   
@Retention(RetentionPolicy.RUNTIME) 
public @interface Sql {
	/**对应xml 配置中的 mapper */
    String mapper() default Strings.BLANK;
	/** sql语句 */
    String sql()  default Strings.BLANK;
	/**sql类型*/
    String sqlType() default Strings.BLANK;
	/**模板类型*/
    String template() default Strings.BLANK;
	/**包含文件名称*/
    String include() default Strings.BLANK;
	/**语句类型 select update delete insert*/
    String type();
	/**是否是量*/
    boolean batch() default false;
	
}
