package org.smile.orm.ann;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.smile.annotation.AliasFor;
import org.smile.commons.Strings;
import org.smile.commons.ann.NULL;

@Target(ElementType.FIELD)   
@Retention(RetentionPolicy.RUNTIME) 
@Association(foreignKey = Strings.BLANK,type=Association.manyToOne)
public @interface ManyToOne
{
	/**外键*/
	@AliasFor(annotation = Association.class) String foreignKey();
	/**关联的实体类*/
	@AliasFor(annotation = Association.class) Class<?> className() default NULL.class;
}
