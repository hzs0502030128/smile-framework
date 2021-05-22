package org.smile.cache.plugin.ann;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.smile.commons.Strings;
/**
 * 对缓存移除操作的注解
 * @author 胡真山
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CacheWipe {
	/**缓存的键*/
	String key() ;
	/**
	 * 参数名称
	 * @return
	 */
	String [] fields() default {};
	/**
	 * 是否是需要循环移除缓存
	 * 
	 * 列如：
	 *    参数  update(List list)   配置了 fields=list[*].name 
	 *    那么就可以使用循环移除的方式
	 *    就会循环对name属性的缓存移除
	 */
	boolean loop() default false;
	/**
	 * 是否忽略参数值
	 * @return
	 */
	boolean ignoreArgs() default false;
	
	/***
	 * 使用缓存的方案 为空时使用默认方案
	 * @return 方案名称
	 */
	String scheme() default Strings.BLANK;

}
