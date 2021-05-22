package org.smile.beans;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.smile.beans.converter.BasicConverter;
import org.smile.beans.converter.BeanException;
import org.smile.beans.converter.Converter;
import org.smile.collection.IndexList;
import org.smile.commons.Chars;
import org.smile.reflect.ClassTypeUtils;

/***
 * bean 属性操作类
 * 
 * @author 胡真山 2015年9月1日
 */
public class BeanProperties implements PropertyHandler<Object>{
	/**默认的 属性区分大小写 不存在属性时抛出异常*/
	public static final BeanProperties NORAL=new BeanProperties();
	/**不区分大小写*/
	public static final BeanProperties NOCASE=new BeanProperties(PropertyKeyType.nocase);
	/**不存在属性时不抛出异常*/
	public static final BeanProperties NORAL_CAN_NO_PROPERTY=new BeanProperties(false);

	private Converter converter = BasicConverter.getInstance();

	/** 区分大小写 */
	protected PropertyKeyType keyType;
	/***
	 * 不存在属性时是否抛出异常
	 */
	protected boolean notExistsPropertError = true;
	
	public BeanProperties(){
		this.keyType=PropertyKeyType.normal;
	}
	
	/**
	 * 是否需要在不存在属性时抛出异常
	 * @param notExistsPropertError
	 */
	public BeanProperties(boolean notExistsPropertError){
		this.keyType=PropertyKeyType.normal;
		this.notExistsPropertError=notExistsPropertError;
	}
	
	public BeanProperties(PropertyKeyType keyType,boolean notExistsPropertError){
		this.keyType=keyType;
		this.notExistsPropertError=notExistsPropertError;
	}
	

	public BeanProperties(PropertyKeyType keyType){
		this.keyType = keyType;
	}

	/***
	 * @param source   属性的值从map中获取填充到目标对象中
	 * @return
	 * @throws BeanException
	 */
	public void populate(Map<String, Object> source,Object target) throws BeanException {
		Class clazz=target.getClass();
		for (Map.Entry<String, Object> entry : source.entrySet()) {
			PropertyDescriptor pd = keyType.getBeanInfoProperty(BeanInfo.getInstance(clazz), entry.getKey());
			if (pd != null) {
				setFieldValue(entry.getValue(), target, pd);
			}
		}
	}

	public PropertyDescriptor getPropertyDescriptor(Class clazz, String name) throws BeanException {
		BeanInfo beanInfo = BeanInfo.getInstance(clazz);
		if (beanInfo == null) {
			return null;
		} else {
			return keyType.getBeanInfoProperty(beanInfo, name);
		}
	}

	/**
	 * 设置一个对象的属性表达式
	 * 
	 * @param fieldName 字段属性名表达式 可以是.递归设置
	 * @param value 要设置的值
	 * @param targetBean 目标对象
	 * @throws BeanException
	 */
	@Override
	public void setExpFieldValue(Object targetBean,String fieldName, Object value) throws BeanException {
		int index = fieldName.indexOf(Chars.DOT);
		if (index > 0) {
			String name = fieldName.substring(0, index);
			PropertyValue<?> subObject = getFieldValue(targetBean, name, true);
			if (PropertyValue.notNull(subObject)) {
				setExpFieldValue(subObject, fieldName.substring(index+1), value);
			}
		} else {
			try {
				setFieldValue(fieldName, value, targetBean);
			} catch (Exception e) {
				throw new AccessPropertyException("设置" + targetBean + "的属性" + fieldName + " 值:" + value + "出错", e);
			}
		}
	}
	/**
	 * 对一个属性值进行赋值
	 * @param subObject
	 * @param subFieldName
	 * @param value
	 * @throws BeanException
	 */
	public void setExpFieldValue(PropertyValue subObject, String subFieldName,Object value) throws BeanException{
		if (Collection.class.isAssignableFrom(subObject.type())) {
			subObject.setCollectionPropertyValue(this, subFieldName, value);
		} else if (Map.class.isAssignableFrom(subObject.type())) {
			subObject.setPropertyValue(this, subFieldName, value);
		} else {
			setExpFieldValue( subObject.value(),subFieldName, value);
		}
	}

	/***
	 * 获取表达式的属性的值
	 * 
	 * @param targetBean 用于取值的对象
	 * @param exp 需取值的属性表达式
	 * @return 取得的值
	 * @throws BeanException
	 */
	@Override
	public Object getExpFieldValue(Object targetBean, String exp) throws BeanException {
		int index = exp.indexOf(Chars.DOT);
		if (index > 0) {
			String name = exp.substring(0, index);
			PropertyValue<?> value = getFieldValue(targetBean, name, false);
			if (PropertyValue.notNull(value)) {
				String subName = exp.substring(index + 1);
				return getExpFieldValue(value, subName);
			}
			return null;
		}
		return getFieldValue(targetBean, exp);
	}

	/**
	 * 获取表达式的属性
	 * @param value
	 * @param exp
	 * @return
	 * @throws BeanException
	 */
	public Object getExpFieldValue(PropertyValue value, String exp) throws BeanException {
		if (Collection.class.isAssignableFrom(value.type())) {
			return value.getCollectionPropertyValue(this, exp);
		}else {
			return value.getPropertyValue(this, exp);
		}
	}

	/**
	 * 获取一个对象的一个属性
	 * 
	 * @param targetObject 目标对象
	 * @param name 属性名
	 * @return
	 * @throws BeanException
	 */
	public Object getFieldValue(Object targetObject, String name) throws BeanException {
		PropertyValue<?> pv= getFieldValue(targetObject, name, false);
		if(pv!=null){
			return pv.value();
		}
		return null;
	}

	/***
	 * 获取一个对象的一个属性的值
	 * 
	 * @param targetObject
	 * @param name
	 * @param needInit 是否需要进行初始化
	 * @return
	 * @throws Exception
	 */
	public PropertyValue getFieldValue(Object targetObject, String name, boolean needInit) throws BeanException {
		Class<?> clazz = targetObject.getClass();
		BeanInfo beanInfo = BeanInfo.getInstance(clazz);
		PropertyDescriptor property = keyType.getBeanInfoProperty(beanInfo, name);
		if (property != null) {
			Method reader = property.getReadMethod();
			if (reader == null) {
				throw new NoPropertyException("不存在   getter: " + name + " 在" + clazz);
			}
			try {
				Object value = reader.invoke(targetObject);
				Class<?> type = reader.getReturnType();
				if (value == null && needInit) {//需地初化为空的属性
					if (type == List.class) {
						value = new IndexList();
					} else if (type == Set.class) {
						value = new HashSet();
					} else if (type == Map.class) {
						value = new HashMap();
					} else if(type==Collection.class){
						value = new IndexList();
					}else{
						value=type.newInstance();
					}
					setFieldValue(name, value, targetObject);
					value = reader.invoke(targetObject);
				}
				return new PropertyValue(value, type, ClassTypeUtils.getGenericObj(reader.getGenericReturnType()));
			} catch (Exception e) {
				throw new BeanException(clazz + " getter 方法" + reader.getName(), e);
			}
		} else if (this.notExistsPropertError) {
			throw new NoPropertyException("不存在的属性:" + name + " 在类:" + clazz);
		} else {
			return null;
		}
	}

	public void setFieldValue(Object value, Object targetBean, PropertyDescriptor pd) throws BeanException {
		Method setter = pd.getWriteMethod();
		if (checkMethod(pd, setter, targetBean)) {
			try {
				Type[] types = setter.getGenericParameterTypes();
				setter.invoke(targetBean, converter.convert(pd.getPropertyType(), ClassTypeUtils.getGenericObj(types[0]), value));
			} catch (Exception e) {
				throw new BeanException("属性:" + pd.getName() + " setter 方法出错,value:" + value, e);
			}
		}
	}

	protected boolean checkMethod(PropertyDescriptor pd, Method method, Object targetBean) throws BeanException {
		if (method == null) {
			if (notExistsPropertError) {
				throw new BeanException("不存在的 方法:" + pd.getName() + " 在类:" + targetBean.getClass());
			} else {
				return false;
			}
		}
		return true;
	}

	/**
	 * @param name
	 * @param value
	 * @param bean
	 * @param noPropertyError 没有此属性 或方法是否异常
	 * @throws BeanException
	 */
	public void setFieldValue(String name, Object value, Object bean) throws BeanException {
		if (value == null || bean == null) {
			return;
		}else {
			Class clazz = bean.getClass();
			BeanInfo beanInfo =BeanInfo.getInstance(clazz);
			PropertyDescriptor property = keyType.getBeanInfoProperty(beanInfo, name);
			if (checkProperty(property,name, clazz)) {
				setFieldValue(value, bean, property);
			}
		}
	}

	/**
	 * 验证属性 是否为空 是否需要抛出异常
	 * @param property  判断是否为空 
	 * @param name 属性名称
	 * @param clazz  类
	 * @return
	 * @throws BeanException
	 */
	protected boolean checkProperty(PropertyDescriptor property,String name, Class clazz) throws NoPropertyException {
		if (property == null) {
			if (this.notExistsPropertError) {
				throw new NoPropertyException("不存在的属性:" + name + " 在类:" + clazz);
			} else {
				return false;
			}
		}
		return true;
	}

	public Converter getConverter() {
		return converter;
	}

	
	public void setConverter(Converter converter) {
		this.converter = converter;
	}
	
	/**
	 * 是否存在字段
	 * @param name
	 * @return
	 */
	public boolean hasField(BeanInfo beanInfo,String name){
		return keyType.getBeanInfoProperty(beanInfo,name)!=null;
	}
}
