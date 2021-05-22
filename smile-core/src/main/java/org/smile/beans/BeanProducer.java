package org.smile.beans;

import org.smile.beans.converter.BeanException;

public interface BeanProducer {
	/**
	 *     获取实例对象
	 * @return
	 * @throws BeanException
	 */
	public Object getBean() throws BeanException;
	/**
	 * 创建bean的类型
	 */
	public Class getBeanClass() ;
	/**
	 * bean 的id属性
	 * @return
	 */
	public String getBeanId();
}
