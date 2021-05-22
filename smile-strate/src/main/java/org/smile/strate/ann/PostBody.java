package org.smile.strate.ann;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.smile.annotation.AliasFor;
import org.smile.commons.Strings;

@Target(ElementType.METHOD)   
@Retention(RetentionPolicy.RUNTIME)
@PostMapping
@RequestBody
public @interface PostBody {
	/**名称为空是会使用方法名*/
	@AliasFor(annotation = PostMapping.class)
	public String url() default Strings.BLANK;
	/**跳转结果*/
	@AliasFor(annotation = PostMapping.class)
	public Result[] results() default {@Result(type="json")};
	/** 用于标记接收请求体内容的参数是否可为空 */
	@AliasFor(annotation = RequestBody.class)
	public boolean requried() default false;
	
}
