package org.smile.orm.base.impl;

import java.sql.SQLException;
import java.util.Map;

import org.smile.beans.converter.BasicConverter;
import org.smile.beans.converter.ConvertException;
import org.smile.beans.converter.Converter;
import org.smile.db.handler.MapRowHandler;
import org.smile.db.handler.ResultSetMap;
import org.smile.db.result.BeanResultParser;
import org.smile.db.result.ResultParser;
import org.smile.orm.mapping.OrmMapping;
import org.smile.orm.mapping.OrmObjMapping;
import org.smile.orm.mapping.property.OrmProperty;

/**
 *  按照注解配置的字段映射转换成map结果集
 * 列名会转换成属性
 * @author 胡真山
 */
public class OrmMapRowHandler extends MapRowHandler {
	/**结果集解析*/
	protected static ResultParser parser = new BeanResultParser();
	/**结果集的映射*/
	protected OrmMapping ormMapper=null;
	/**类型转换器，处理属性转换*/
	protected Converter converter=BasicConverter.getInstance();


	public OrmMapRowHandler(Class mapperClass) {
		this.resultClass = ResultSetMap.class;
		ormMapper =OrmObjMapping.getOrmMapper(mapperClass);
	}


	@Override
	protected void setValue(Map<String, Object> result, String columnName, Object value) throws SQLException {
		OrmProperty property=ormMapper.getPropertyByColumn(columnName);
		try {
			value=converter.convert(property.getFieldType(), value);
		} catch (ConvertException e) {
			throw new SQLException(e);
		}
		result.put(property.getPropertyName(), value);
	}


	public void setConverter(Converter converter) {
		this.converter = converter;
	}
	
	
}
