package org.smile.beans;

import org.smile.beans.converter.BeanException;

public interface BeanFactorySupport<E extends BeanProducer> {
	/**
	 * 获取bean 
	 * @param id 要获取bean的配置id
	 * @return
	 * @throws BeanException
	 */
	public abstract <T> T getBean(String id) throws BeanException;
	/**
	 * 
	 * @param <T>
	 * @param id
	 * @param notExsitsError 不存在时是否抛出异常
	 * @return
	 * @throws BeanException
	 */
	public abstract <T> T getBean(String id,boolean notExsitsError) throws BeanException;
	/**
	 * 以类名从工厂获取bean实例
	 * @param beanClass
	 * @return
	 * @throws BeanException
	 */
	public abstract <T> T getBean(Class<T> beanClass) throws BeanException; 
	
	/**
	 * 	注册生产者
	 * @param id
	 * @param producter
	 */
	public void registProducer(String id,E producter);

}
