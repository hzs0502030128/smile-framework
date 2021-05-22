package org.smile.strate.ann;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.smile.annotation.AliasFor;
import org.smile.commons.Strings;
import org.smile.commons.ann.Bean;

@Target({ElementType.TYPE})   
@Retention(RetentionPolicy.RUNTIME)
@Bean
@ActionPackage(extend=Strings.BLANK,results= {@Result(type="json")})
public @interface Controller {
	/**包名*/
	@AliasFor(annotation = ActionPackage.class)
	public String name() default Strings.BLANK;
	/**
	 * 继承包
	 * @return
	 */
	@AliasFor(annotation = ActionPackage.class)
	public String extend() default Strings.BLANK;
	/**
	 * 路径空间
	 * @return
	 */
	@AliasFor(value="namespace",annotation = ActionPackage.class)
	public String path() default Strings.BLANK;
	/**
	 * 默认的拦截器参考
	 * @return
	 */
	@AliasFor(annotation = ActionPackage.class)
	public String interceptorRef() default Strings.BLANK;
	
	/**跳转结果*/
	@AliasFor(annotation = ActionPackage.class)
	public Result[] results() default {}; 
}
