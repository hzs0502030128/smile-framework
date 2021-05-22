package org.smile.strate.ann;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.smile.commons.Strings;
import org.smile.strate.action.Action;

@Target(ElementType.METHOD)   
@Retention(RetentionPolicy.RUNTIME)
public @interface Result {
	/**
	 * 跳转结果名称
	 * @return
	 */
	public String name() default Action.SUCCESS;
	/**
	 * 跳转结果类型
	 * @return
	 */
	public String type() default Strings.BLANK;
	/**
	 * 跳转结果的内容
	 * @return
	 */
	public String value() default Strings.BLANK;
}
