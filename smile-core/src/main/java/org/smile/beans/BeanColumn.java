package org.smile.beans;

import org.smile.beans.converter.BeanException;
import org.smile.commons.Column;
/**
 * 一个javabean类型的列封闭
 * @author 胡真山
 * @Date 2016年1月8日
 * @param <T>
 */
public interface BeanColumn<T> extends Column<T>{
	/**
	 * 往目标对象中的属性列赋值
	 * @param target 目标对象
	 * @param value 需要赋的值
	 * @throws BeanException 
	 */
	public void writeValue(Object target,Object value) throws BeanException ;
	/**
	 * 获取一个属性列的值
	 * @param target 目标对象
	 * @return 属性的值
	 * @throws BeanException
	 */
	public Object getValue(Object target) throws BeanException ;
}
