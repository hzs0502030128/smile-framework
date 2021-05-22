package org.smile.strate.ann;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.smile.commons.Strings;
import org.smile.http.HttpMethod;

@Target({ElementType.METHOD,ElementType.ANNOTATION_TYPE})   
@Retention(RetentionPolicy.RUNTIME)
public @interface Action {
	/**action名称 为空是会使用方法名*/
	public String name() default Strings.BLANK;
	/**跳转结果*/
	public Result[] results() default {}; 
	/**可以接收的请求方法*/
	public HttpMethod[] method() default {}; 
}
