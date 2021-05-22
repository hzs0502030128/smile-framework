package org.smile.db.result;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;

import org.smile.beans.converter.ConvertException;
import org.smile.beans.converter.Converter;
import org.smile.db.JsonDbSerializable;
import org.smile.json.JSON;
import org.smile.reflect.ClassTypeUtils;
import org.smile.util.StringUtils;

public class BaseColumnParser implements ColumnParser{
	
	/**对象类型转换器,用于从数据库字段转成对象*/
	protected Converter converter=ResultUtils.CONVERTER;
	
	@Override
	public Object parseResultColumn(ResultSet rs, DatabaseColumn column) throws SQLException {
		return column.parseResultColumn(converter, rs);
	}
	
	
	@Override
	public Object parseResultColumn(ResultSet rs,String column,Class fieldType) throws SQLException{
		//json保存字段
		if(jsonStore(fieldType)){
			try {
				String str=rs.getString(column);
				if(StringUtils.notEmpty(str)){
					Object value=converter.convert(fieldType, ClassTypeUtils.getGenericObj(fieldType), JSON.parse(str));
					return value;
				}
				return null;
			} catch (ConvertException e) {
				throw new SQLException("转换结果集至对象失败 "+column+"-->"+fieldType,e);
			}
		} else{
			return ColumnUtils.getColumn(fieldType, rs, column);
		}
	}
	
	/**
	 * 是否需要json存贮
	 * @return
	 */
	@Override
	public boolean jsonStore(Class fieldType){
		return 	Map.class.isAssignableFrom(fieldType)||Collection.class.isAssignableFrom(fieldType)||JsonDbSerializable.class.isAssignableFrom(fieldType);
	}
	
	/**
	 * 设置数据转换器
	 * @param converter
	 */
	public void setConverter(Converter converter) {
		this.converter = converter;
	}
	
	

}
