package org.smile.orm.ann;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.smile.commons.ann.NULL;
/**
 * 与其它表关联的字段
 * @author 胡真山
 *
 */
@Target({ElementType.FIELD,ElementType.ANNOTATION_TYPE})   
@Retention(RetentionPolicy.RUNTIME) 
public @interface Association {
	byte oneToOne=1;
	byte oneToMany=2;
	byte manyToOne=3;
	/**外键*/
    String foreignKey();
	/**关联的实体类*/
    Class<?> className() default NULL.class;
	/**
	 * 关联的类型
	 * @return
	 */
    byte type() ;
}
