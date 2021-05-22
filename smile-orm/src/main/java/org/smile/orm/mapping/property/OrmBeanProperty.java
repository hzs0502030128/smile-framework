package org.smile.orm.mapping.property;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Type;

import org.smile.beans.BeanProperties;
import org.smile.beans.BeanUtils;
import org.smile.beans.PropertyValue;
import org.smile.beans.converter.BeanException;
import org.smile.db.result.ResultUtils;
import org.smile.orm.OrmInitException;
import org.smile.orm.mapping.FieldPropertyException;
import org.smile.reflect.ClassTypeUtils;

/**
 * 以beanproperty 绑定的映射
 * @author 胡真山
 *
 */
public class OrmBeanProperty extends OrmProperty{
	/**对应的bean属性*/
	protected PropertyDescriptor beanProperty;
	
	/**
	 * 
	 * @param property javabean property
	 */
	public OrmBeanProperty(PropertyDescriptor property) {
		setPropertyDescriptor(property);
	}

	@Override
	public Object readValue(Object bean) {
		if(isComponentProperty()) {
			try {
				Object componetValue=BeanProperties.NORAL.getFieldValue(bean,this.component.getName());
				if(componetValue==null) {
					return null;
				}
				return BeanUtils.getObjectProperty(componetValue, beanProperty);
			} catch (BeanException e) {
				throw new OrmInitException("write property "+propertyName,e);
			}
		}else {
			try {
				return BeanUtils.getObjectProperty(bean, beanProperty);
			} catch (BeanException e) {
				throw new FieldPropertyException("读取"+bean+"字段"+beanProperty.getName()+"出错",e);
			}
		}
	}

	@Override
	public void writeValue(Object bean, Object value) {
		if(isComponentProperty()) {
			try {
				PropertyValue<?> componetValue=BeanProperties.NORAL.getFieldValue(bean,this.component.getName(), true);
				BeanUtils.setObjectProperty(componetValue.value(),beanProperty, value);
			} catch (BeanException e) {
				throw new OrmInitException("write property "+propertyName,e);
			}
		}else {
			try {
				BeanUtils.setObjectProperty(bean,beanProperty, value);
			} catch (BeanException e) {
				throw new FieldPropertyException("设置"+bean+"字段"+beanProperty.getName()+"的值"+value+"出错",e);
			}
		}
	}
	/**
	 * 设置绑定的bean 属性
	 * @param property
	 */
	public void setPropertyDescriptor(PropertyDescriptor property) {
		fieldType = property.getPropertyType();
		Type propertyType=ClassTypeUtils.getGenericType(property);
		this.generic=ClassTypeUtils.getGenericObj(propertyType);
		this.beanProperty = property;
		this.jsonStore=ResultUtils.jsonStore(fieldType);
	}
	

}
