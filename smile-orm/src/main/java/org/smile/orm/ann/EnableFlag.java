package org.smile.orm.ann;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 这是一个用于逻辑删除功能标记的标签
 * @author 胡真山
 * @Date 2016年2月25日
 */
@Target({ElementType.FIELD,ElementType.ANNOTATION_TYPE})   
@Retention(RetentionPolicy.RUNTIME) 
public @interface EnableFlag {
	/**启用时的值*/
    String enable() default "true";
	/**不启用时的值*/
    String disable() default "false";
	
}
