package org.smile.orm.mapping.property;

import java.lang.reflect.Field;

import org.smile.beans.BeanProperties;
import org.smile.beans.PropertyValue;
import org.smile.db.result.ResultUtils;
import org.smile.orm.OrmInitException;
import org.smile.orm.mapping.FieldPropertyException;
import org.smile.reflect.ClassTypeUtils;

/**
 * 与数据库字段映射的属性封装
 * @author 胡真山
 * @Date 2016年1月8日
 */
@SuppressWarnings("rawtypes")
public class OrmFieldProperty extends OrmProperty{
	
	/**对应类的属性字段*/
	private Field field;
	
	
	public OrmFieldProperty(Field field){
		setFieldTypes(field);
	}
	
	@Override
	public Object readValue(Object bean) {
		if(isComponentProperty()) {
			try {
				Object componetValue=BeanProperties.NORAL.getFieldValue(bean,this.component.getName());
				if(componetValue==null) {
					return null;
				}
				return field.get(componetValue);
			} catch (Exception e) {
				throw new OrmInitException("write property "+propertyName,e);
			}
		}else {
			try {
				return field.get(bean);
			} catch (Exception e) {
				throw new FieldPropertyException("读取"+bean+"字段"+field.getName()+"出错",e);
			}
		}
	}
	/**
	 * 往对象就的该字段设置值
	 * @param bean 目标对象
	 * @param value 要设置的值
	 */
	@Override
	public void writeValue(Object bean, Object value) {
		if(isComponentProperty()) {
			try {
				PropertyValue componetValue=BeanProperties.NORAL.getFieldValue(bean,this.component.getName(), true);
				field.set(componetValue.value(), value);
			} catch (Exception e) {
				throw new OrmInitException("write property "+propertyName,e);
			}
		}else {
			try {
				field.set(bean, value);
			} catch (Exception e) {
				throw new FieldPropertyException("设置"+bean+"字段"+field.getName()+"的值"+value+"出错",e);
			}
		}
	}
	/**
	 * 设置映射绑定的字段
	 * @param field
	 */
	public void setFieldTypes(Field field) {
		fieldType = field.getType();
		this.generic=ClassTypeUtils.getGenericObj(field.getGenericType());
		this.field = field;
		this.field.setAccessible(true);
		this.jsonStore=ResultUtils.jsonStore(fieldType);
	}
	
}
