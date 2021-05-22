package org.smile.orm.ann;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 这是一个用于标记使用数组参数占位符
 * @author 胡真山
 * @Date 2016年2月25日
 */
@Target(ElementType.METHOD)   
@Retention(RetentionPolicy.RUNTIME) 
public @interface ArrayBound {
}
