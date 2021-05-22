package org.smile.db.result;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.smile.beans.BeanUtils;
import org.smile.beans.converter.BeanException;
import org.smile.beans.converter.Converter;
import org.smile.commons.NotImplementedException;
import org.smile.commons.SmileRunException;
import org.smile.reflect.Generic;
/**
 * 	基础数据类型列
 * @author 胡真山
 *
 */
public class BasicDatabaseColumn extends AbstractDatabaseColumn<Class> {
	/**
	 * 列类型
	 */
	protected Class resultType;
	
	private Generic generic;
	
	public BasicDatabaseColumn(Class resultType,int index,String columnName){
		this.resultType=resultType;
		this.index=index;
		this.columnName=columnName;
	}

	public void setGeneric(Generic generic) {
		this.generic = generic;
	}

	@Override
	public Class getPropertyType() {
		return resultType;
	}

	@Override
	public Class getProperty() {
		return resultType;
	}

	@Override
	public void writeValue(Object target, Object value) {
		throw new NotImplementedException("not support this method ");
	}

	@Override
	public Object readValue(Object target) {
		throw new NotImplementedException("not support this method ");
	}

	@Override
	public String getColumnName() {
		return columnName;
	}

	@Override
	public boolean isJsonStore() {
		return ResultUtils.jsonStore(resultType);
	}

	@Override
	public Generic getGeneric() {
		return generic;
	}

	@Override
	public void parseResultColumn(Converter converter, ResultSet rs, Object target) throws SQLException {
		Object value=ResultUtils.getColumn(rs, index, resultType);
		try {
			BeanUtils.setExpValue(target, name, value);
		} catch (BeanException e) {
			throw new SmileRunException(columnName+ "-->"+name,e);
		}
	}

}
