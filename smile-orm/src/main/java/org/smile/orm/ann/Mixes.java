package org.smile.orm.ann;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.smile.commons.Strings;
import org.smile.commons.ann.NULL;

@Target({ElementType.TYPE})   
@Retention(RetentionPolicy.RUNTIME)
public @interface Mixes {
	/**使用语句混合方式*/
    String sql() default Strings.BLANK;
	/**以实例方法混合*/
    Class entity() default NULL.class;
	/**外键发展名*/
    String primaryKey();
	/**是否是不连动加载*/
    boolean lazy() default true;
}
