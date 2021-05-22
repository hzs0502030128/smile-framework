package org.smile.orm.ann;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.smile.commons.Strings;
import org.smile.commons.ann.NULL;

@Target({ElementType.FIELD,ElementType.ANNOTATION_TYPE,ElementType.METHOD})   
@Retention(RetentionPolicy.RUNTIME) 
/**
 * 数据库字段属性注解
 * @author 胡真山
 * @Date 2016年3月3日
 */
public @interface Property
{
	/**列名*/
    String column() default Strings.BLANK;
	/**是否是自增*/
    boolean autoincrement() default false;
	/**是否存贮数据库*/
    boolean store() default true;
	/**是否是主键*/
    boolean primaryKey() default false;
	/**是不是uuid生成*/
    boolean uuid() default false;
	/**ID生成器*/
    Class idGenerator() default NULL.class;
}
