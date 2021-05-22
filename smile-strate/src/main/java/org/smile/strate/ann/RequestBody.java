package org.smile.strate.ann;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER,ElementType.ANNOTATION_TYPE})   
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestBody {
	/**
	 *	用于标记接收请求体内容是否可以为空
	 * @return
	 */
	public boolean requried() default false;
	
}
