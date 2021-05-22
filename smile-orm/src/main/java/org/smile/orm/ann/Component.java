package org.smile.orm.ann;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 表组件映射注解
 * @author 胡真山
 */
@Target({ElementType.FIELD,ElementType.ANNOTATION_TYPE})   
@Retention(RetentionPolicy.RUNTIME) 
public @interface Component
{
}
