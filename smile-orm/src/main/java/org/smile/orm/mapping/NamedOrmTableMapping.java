package org.smile.orm.mapping;

import java.lang.reflect.Field;

import org.smile.orm.mapping.property.NamedOrmFieldProperty;
import org.smile.orm.mapping.property.OrmFieldProperty;



/**
 * 与数据库表对象的一个mapper 生成sql语句时使用:name 做为占位符
 * @author 胡真山
 * @param <V>
 */
public class NamedOrmTableMapping<V> extends OrmTableMapping<V>{
	@Override
	public OrmFieldProperty newOrmProperty(Field field) {
		return new NamedOrmFieldProperty(field);
	}
	
}
