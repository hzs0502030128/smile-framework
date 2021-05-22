package org.smile.beans;

import java.util.Collection;

import org.smile.beans.converter.BeanException;
import org.smile.reflect.ClassTypeUtils;
import org.smile.reflect.Generic;
/**
 * 用于封装一一个属性的值
 * @author 胡真山
 * @Date 2016年1月19日
 * @param <T>
 * @param <G>
 */
public class PropertyValue<T>{
	/**
	 * 属性的值
	 */
	private T value;
	/**属性的定义*/
	private final FieldDeclare<T> declare;
	/**
	 * 
	 * @param value 属性的值
	 * @param declare 属性的定义
	 */
	public PropertyValue(T value,FieldDeclare<T> declare) {
		this.value = value;
		this.declare=declare;
	}
	/**
	 * 
	 * @param value
	 * @param type 属性的类型
	 * @param generic 属性类型的泛型
	 */
	public PropertyValue(T value,Class<T> type,Generic generic) {
		this.value = value;
		this.declare=new FieldDeclare<T>(type, generic);;
	}
	/**
	 * @param type
	 * @param generic 泛型封装
	 */
	public PropertyValue(Class<T> type,Generic generic) {
		this.declare=new FieldDeclare<T>(type, generic);
	}

	/**
	 * 类型中值的类型获取
	 * 同时指定泛型
	 */
	public PropertyValue(T value, Class<?>[] generic) {
		this.value = value;
		Class<T> type=(Class<T>) value.getClass();
		this.declare=new FieldDeclare<T>(type, generic);
	}
	/**
	 * 无泛型的数据属性
	 * @param value 属性的值
	 */
	public PropertyValue(T value) {
		this.value = value;
		Class<T> type=(Class<T>) value.getClass();
		this.declare=new FieldDeclare<T>(type);
	}
	/**
	 * 定义一个属性只有类型值为空
	 * @param declare
	 */
	public PropertyValue(FieldDeclare declare) {
		this(null, declare);
	}

	/**
	 * 无泛型的数据属性
	 * @param data 属性的值
	 */
	public PropertyValue(Class<T> type ) {
		this.declare=new FieldDeclare<T>(type);
	}

	/**
	 * 读取值
	 * @return
	 */
	public T value() {
		//如果是为空并是基础类型的时候默认值
		if(value==null&& ClassTypeUtils.isBasicType(declare.type)){
			return ClassTypeUtils.basicNullDefault(declare.type);
		}
		return value;
	}
	/**
	 * 赋值value
	 * @param value
	 */
	public void value(Object value){
		this.value=declare.castValue(value);
	}
	
	public void value(BeanProperties pb,Object value){
		this.value=declare.castValue(pb,value);
	}

	/**
	 * 读取属性的类型
	 * @return
	 */
	public Class<T> type() {
		return declare.type;
	}

	/**
	 * 直接获取属性值 
	 * @param name 非表达式的属性名
	 * @return
	 * @throws BeanException
	 */
	public Object get(Object name) throws BeanException{
		return declare.get(value, name);
	}
	/**
	 * 设置值 不带.号
	 * @param bp
	 * @param key
	 * @param value
	 * @throws BeanException
	 */
	public void set(BeanProperties bp,Object key,Object value) throws BeanException{
		this.value=(T) declare.set(value, bp, String.valueOf(key), value);
	}
	
	/**
	 * 获取此对象的属性值
	 * @param bp
	 * @param fieldname
	 * @return
	 * @throws BeanException
	 */
	public Object getCollectionPropertyValue(BeanProperties bp, String fieldname) throws BeanException{
		return declare.getCollectionPropertyValue((Collection)value, bp, fieldname);
	}
	/**
	 * 获取此对象的属性值
	 * @param bp
	 * @param fieldname
	 * @return
	 * @throws BeanException
	 */
	public Object getPropertyValue(BeanProperties bp, String fieldname) throws BeanException{
		return declare.getExpValue(value, bp, fieldname);
	}
	/**
	 * 设置此属性的值
	 * 只有当类型为map时才调用些方法
	 * @param bp
	 * @param fieldname
	 * @param value
	 */
	public void setPropertyValue(BeanProperties bp, String fieldname, Object value) throws BeanException{
		if(this.value==null){
			this.value=declare.newInstance();
		}
		declare.setExpValue(this.value, bp, fieldname, value);
	}
	
	/**
	 * 设置此属性的值
	 * 只有当类型为List时才调用些方法
	 * @param bp
	 * @param fieldname
	 * @param value
	 */
	public void setCollectionPropertyValue(BeanProperties bp, String fieldname, Object value) throws BeanException{
		this.value=(T)declare.setCollectionPropertyValue(this.value, bp, fieldname, value);
	}

	/**
	 * 属性的值是否为空
	 * @param pv
	 * @return
	 */
	public static boolean isNull(PropertyValue pv){
		return pv==null||pv.value==null;
	}
	/**
	 * 判断属性的值是否是不为空
	 * @param pv
	 * @return
	 */
	public static boolean notNull(PropertyValue pv){
		return !isNull(pv);
	}

	/**
	 * 属性的定义
	 * @return
	 */
	public FieldDeclare<T> getDeclare() {
		return declare;
	}

	/**
	 * 是否指定的泛型的定义
	 * @return
	 */
	public boolean hasGeneric() {
		return declare.hasGeneric();
	}
	
	
	@Override
	public String toString(){
		return declare.type+":"+value;
	}
}
