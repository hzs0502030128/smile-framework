package org.smile.db.handler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.smile.commons.Strings;

/**用于标记是bean封装返回*/
@Target(ElementType.TYPE)   
@Retention(RetentionPolicy.RUNTIME) 
public @interface LikeBeanMapper{
	/**映射名称*/
	public String name() default Strings.BLANK;
}
