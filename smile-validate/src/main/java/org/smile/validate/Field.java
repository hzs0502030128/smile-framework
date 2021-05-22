package org.smile.validate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 配置对一个字段的验证
 * @author 胡真山
 * @Date 2016年1月28日
 */
@Target(ElementType.METHOD)   
@Retention(RetentionPolicy.RUNTIME) 
public @interface Field {
	/**要验证的字段名*/
	public String name();
	/**验证类型*/
	public ValidateType type();
	/**数字区间,在数据大小区间 字符串长度是用到 等 数字类型的参数*/
	public String[] range() default {};
	/**字符串的值   在eqaul 和 notequal 等字符串判断是用到*/
	public String value() default "";
	/**正则表达式匹配时用到*/
	public String regexp() default "";
	/**不通过时字段名称提示语*/
	public String text() default "";
	/**不通过时字段名称提示语国际化字符名称*/
	public String key() default "";
	/**假如为空时*/
	public boolean ifnull() default false;
	/**自定义验证实现类*/
	public Class<? extends CustomValidator> custom() default NULLValidater.class; 
}
