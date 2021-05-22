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
@RequestMapping(method=HttpMethod.POST)
public @interface PostMapping {
	/**名称 为空是会使用方法名*/
	@AliasFor(annotation = RequestMapping.class)
	public String url() default Strings.BLANK;
	/**跳转结果*/
	@AliasFor(annotation = RequestMapping.class)
	public Result[] results() default {@Result(type="json")};
}
