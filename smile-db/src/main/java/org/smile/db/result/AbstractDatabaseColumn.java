package org.smile.db.result;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.smile.beans.converter.ConvertException;
import org.smile.beans.converter.Converter;
import org.smile.json.JSON;
import org.smile.util.StringUtils;

public abstract class AbstractDatabaseColumn<T> implements DatabaseColumn<T>{
	/**属性索引*/
	protected int index;
	/**数据库列名*/
	protected String columnName;
	
	/**是否是json字符串存贮*/
	protected boolean jsonStore;
	/**
	 * 属性名称
	 */
	protected String name;
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public String getColumnName() {
		return columnName;
	}

	@Override
	public boolean isJsonStore() {
		return jsonStore;
	}
	
	@Override
	public int getIndex() {
		return index;
	}
	
	@Override
	public Object parseResultColumn(Converter converter,ResultSet rs) throws SQLException{
		Class fieldType = getPropertyType();
		int index=getIndex();
		//json保存字段
		if(isJsonStore()){
			try {
				String str=rs.getString(index);
				if(StringUtils.notEmpty(str)){
					Object value=converter.convert(fieldType, getGeneric(), JSON.parse(str));
					return value;
				}
				return null;
			} catch (ConvertException e) {
				throw new SQLException("转换结果集至对象失败"+index+"-"+getColumnName()+"-->"+fieldType,e);
			}
		} else{
			return ColumnUtils.getColumn(fieldType, rs, index);
		}
	}

	@Override
	public void parseResultColumn(Converter converter,ResultSet rs, Object target) throws SQLException {
		Object value=parseResultColumn(converter, rs);
		writeValue(target, value);
	}
}
