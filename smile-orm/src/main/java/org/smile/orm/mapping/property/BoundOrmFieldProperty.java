package org.smile.orm.mapping.property;

import java.lang.reflect.Field;

/**
 * ORM 字段属性    
 * 重写了 占位符 方式   %{name} 格式
 * @author 胡真山
 * @Date 2016年1月8日
 */
public class BoundOrmFieldProperty extends OrmFieldProperty {

	public BoundOrmFieldProperty(Field field) {
		super(field);
	}

	@Override
	public String getPropertyExp() {
		return "%{"+propertyName+"}";
	}
	
}
