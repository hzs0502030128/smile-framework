package org.smile.beans;

import org.smile.beans.converter.BeanException;
/**
 * 属性处理接口
 * 
 * 获取属性值  设置属性值
 * @author 胡真山
 * @Date 2016年2月2日
 */
public interface PropertyHandler<T>{
	/**
	 * 获取一个对象的属性 
	 * @param target 目标对象
	 * @param exp 可以是表达式的属性
	 * @return
	 * @throws BeanException
	 */
	public Object getExpFieldValue(T target,String exp) throws BeanException;
	/**
	 * 设置一个表达式属性的值
	 * @param target 需要设置值的对象
	 * @param exp 表达式属性
	 * @param value
	 * @throws BeanException
	 */
	public void setExpFieldValue(T target,String exp,Object value) throws BeanException;
	
}
