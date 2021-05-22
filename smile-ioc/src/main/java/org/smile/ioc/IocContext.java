package org.smile.ioc;

import org.smile.beans.converter.BeanException;
/**
 * IOC上下文的接口
 * @author 胡真山
 * @Date 2016年4月29日
 */
public interface IocContext {
	/**
	 * ioc的对象工厂
	 * @return
	 */
	public BeanFactory getBeanFactory();
	/**
	 * 获取一个bean 
	 * @param id 配置的配置
	 * @return
	 * @throws BeanException
	 */
	public Object getBean(String id) throws BeanException;
	/**
	 * 释放资源
	 */
	public void distory() ;
	
	public void afterLoad();
	/**
	 * 获取bean以类名形式
	 * @param beanClass 要获取bean的类型
	 * @return
	 * @throws BeanException
	 */
	public <T> T getBean(Class<T> beanClass) throws BeanException;  
}
