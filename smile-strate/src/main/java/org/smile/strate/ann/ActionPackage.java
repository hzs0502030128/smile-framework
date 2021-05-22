package org.smile.strate.ann;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.smile.commons.Strings;

@Target(ElementType.TYPE)   
@Retention(RetentionPolicy.RUNTIME)
public @interface ActionPackage {
	/**包名*/
	public String name() default Strings.BLANK;
	/**
	 * 继承包
	 * @return
	 */
	public String extend();
	/**
	 * 路径空间
	 * @return
	 */
	public String namespace() default Strings.BLANK;
	/**
	 * 默认的拦截器参考
	 * @return
	 */
	public String interceptorRef() default Strings.BLANK;
	
	/**跳转结果*/
	public Result[] results() default {}; 
	
}
