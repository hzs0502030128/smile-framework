package org.smile.db.result;

import java.beans.PropertyDescriptor;

import org.smile.beans.BeanUtils;
import org.smile.beans.converter.BeanException;
import org.smile.db.SqlRunException;
import org.smile.reflect.ClassTypeUtils;
import org.smile.reflect.Generic;
/**
 * 以一个javabean 来映射数据库结果集
 * 的一个列对应
 * @author 胡真山
 *
 */
public class BeanDatabaseColumn extends AbstractDatabaseColumn<PropertyDescriptor>{
	
	/**属性定义*/
	private PropertyDescriptor pd;
	
	/**映射对象属性的泛型*/
	private Generic generic;

	public BeanDatabaseColumn(int index, PropertyDescriptor pd,String columnName) {
		this.index = index;
		this.pd = pd;
		this.columnName=columnName;
		if(pd!=null){
			this.generic=ClassTypeUtils.getGenericObj(ClassTypeUtils.getGenericType(pd));
			this.jsonStore=ResultUtils.jsonStore(pd.getPropertyType());
		}
	}

	

	@Override
	public String getName() {
		return pd.getName();
	}

	@Override
	public Class getPropertyType() {
		return pd.getPropertyType();
	}

	@Override
	public PropertyDescriptor getProperty() {
		return pd;
	}

	@Override
	public void writeValue(Object target, Object value){
		try {
			BeanUtils.setObjectProperty(target, pd, value);
		} catch (BeanException e) {
			throw new SqlRunException("index "+index+" column "+columnName, e);
		}
	}

	@Override
	public Object readValue(Object target) {
		try {
			return BeanUtils.getObjectProperty(target, pd);
		} catch (BeanException e) {
			throw new SqlRunException("index "+index+" column "+columnName, e);
		}
	}


	@Override
	public Generic getGeneric() {
		return generic;
	}
}
