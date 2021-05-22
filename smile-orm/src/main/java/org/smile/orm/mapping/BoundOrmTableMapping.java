package org.smile.orm.mapping;

import java.lang.reflect.Field;

import org.smile.orm.mapping.property.BoundOrmFieldProperty;
import org.smile.orm.mapping.property.OrmFieldProperty;



/**
 * 与数据库表对象的一个mapper
 * 生成sql时使用%{name}方式做为占位符
 * 用于配置ORM接口dao方式执行
 * @author 胡真山
 * @param <V>
 */
public class BoundOrmTableMapping<V> extends OrmTableMapping<V>{
	@Override
	public OrmFieldProperty newOrmProperty(Field field) {
		return new BoundOrmFieldProperty(field);
	}
	
}
