package org.smile.orm.ann;

import org.smile.annotation.AliasFor;
import org.smile.commons.Strings;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Property
public @interface TenantId {
    /**数据库对应列名*/
    @AliasFor(annotation = Property.class) String column() default Strings.BLANK;
}
