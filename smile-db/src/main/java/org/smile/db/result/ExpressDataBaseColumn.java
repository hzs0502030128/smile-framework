package org.smile.db.result;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.smile.beans.BeanProperties;
import org.smile.beans.converter.BeanException;
import org.smile.beans.converter.Converter;
import org.smile.commons.NotImplementedException;
import org.smile.db.SqlRunException;
import org.smile.reflect.Generic;

public class ExpressDataBaseColumn extends AbstractDatabaseColumn<Class>{
	
	public ExpressDataBaseColumn(int index,String columnName) {
		this.index=index;
		this.columnName=columnName;
		this.name=columnName;
	}

	@Override
	public void writeValue(Object target, Object value) {
		try {
			BeanProperties.NORAL.setExpFieldValue(target, name, value);
		} catch (BeanException e) {
			throw new SqlRunException("index "+index+" column "+columnName, e);
		}
	}

	@Override
	public Object readValue(Object target) {
		try {
			return BeanProperties.NORAL.getExpFieldValue(target,name);
		} catch (BeanException e) {
			throw new SqlRunException("index "+index+" column "+columnName, e);
		}
	}

	@Override
	public Generic getGeneric() {
		return null;
	}
	
	@Override
	public Object parseResultColumn(Converter converter,ResultSet rs) throws SQLException{
		return rs.getObject(index);
	}

	@Override
	public void parseResultColumn(Converter converter, ResultSet rs, Object target) throws SQLException {
		Object value=rs.getObject(index);
		try {
			BeanProperties.NORAL.setExpFieldValue(target,name,value);
		} catch (BeanException e) {
			throw new SqlRunException(e);
		}
	}

	@Override
	public Class getPropertyType() {
		throw new NotImplementedException("not support this method ");
	}

	@Override
	public Class getProperty() {
		throw new NotImplementedException("not support this method ");
	}
}
