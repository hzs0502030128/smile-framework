package org.smile.strate.ann;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.smile.annotation.AliasFor;
import org.smile.commons.Strings;
import org.smile.http.HttpMethod;

@Target({ElementType.METHOD,ElementType.ANNOTATION_TYPE})   
@Retention(RetentionPolicy.RUNTIME)
@Action
public @interface RequestMapping{
	/**名称 为空是会使用方法名*/
	@AliasFor(value="name",annotation = Action.class)
	public String url() default Strings.BLANK;
	/**跳转结果*/
	@AliasFor(annotation = Action.class)
	public Result[] results() default {@Result(type="json")}; 
	/**可以接收的请求方法*/
	@AliasFor(annotation = Action.class)
	public HttpMethod[] method() default {}; 
}
