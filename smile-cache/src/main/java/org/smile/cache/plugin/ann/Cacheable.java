package org.smile.cache.plugin.ann;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.smile.commons.Strings;
/**
 * 对缓存与入操作的注解
 * @author 胡真山
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Cacheable{
	/**缓存的key
	 * 这时默认以类名.方法名.方法参数类型,参数值等信息生成
	 * */
	String key() default Strings.BLANK;
	/**缓存过期时间 单位秒
	 * 默认 10秒
	 * */
	int validTime() default 0;
	/**指定参数字段
	 * 只使用参数的部分信息做为生成缓存键的关键信息
	 * */
	String [] fields() default {};
	/**是否只写不读缓存
	 * 如在 update时，只需要写入缓存
	 * 配合get方法时使用：
	 * 设置key与get方法一样时，就会更新缓存，使用get的时候能获取到最新缓存
	 * */
	boolean onlyWrite() default false;
	/**
	 * 是否忽略参数值
	 * @return 如果为true时则不对参数进行区分，不同的参数共用一个缓存
	 */
	boolean ignoreArgs() default false;
	/***
	 * 使用缓存的方案 为空时使用默认方案
	 * @return 方案名称
	 */
	String scheme() default Strings.BLANK;
}
