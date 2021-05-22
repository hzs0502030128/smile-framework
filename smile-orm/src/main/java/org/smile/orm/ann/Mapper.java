package org.smile.orm.ann;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.smile.commons.Strings;
import org.smile.orm.SqlType;

/**
 * 用来注解orm的maper
 * @author 胡真山
 *
 */
@Target(ElementType.TYPE)   
@Retention(RetentionPolicy.RUNTIME) 
public @interface Mapper {
	/**
	 * 映射的名称
	 * @return
	 */
    String name() default Strings.BLANK;
	/**
	 * 映射的命名空间
	 * @return
	 */
    String namespace() default Strings.BLANK;
	/** 
	 * 部分方法实现目标
	 */
    String target() default Strings.BLANK;
	/**
	 * 模板类型
	 * @return
	 */
    String template() default Strings.BLANK;
	/**
	 * 是否单例
	 * @return
	 */
    boolean single() default true;
	/**
	 * sql语句类型
	 * @return
	 */
    String sqlType() default SqlType.SQL;
	
	/**
	 * 包含文件
	 * @return
	 */
    String include() default Strings.BLANK;
}
