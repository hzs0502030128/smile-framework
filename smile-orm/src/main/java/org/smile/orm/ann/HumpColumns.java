package org.smile.orm.ann;

import org.smile.commons.Strings;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记所有的属性使用驼峰映射数据库
 */
@Target(ElementType.TYPE) 
@Retention(RetentionPolicy.RUNTIME) 
public @interface HumpColumns {
	/***/
	String enableField() default Strings.BLANK;
}
