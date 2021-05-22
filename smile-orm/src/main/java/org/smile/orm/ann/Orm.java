package org.smile.orm.ann;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.smile.commons.Strings;

@Target(ElementType.TYPE) 
@Retention(RetentionPolicy.RUNTIME) 
public @interface Orm {
	/**映射的名称*/
	String name() default Strings.BLANK;
	/**
	 * 是否对应
	 * @return
	 */
	boolean isTable() default false;
}
