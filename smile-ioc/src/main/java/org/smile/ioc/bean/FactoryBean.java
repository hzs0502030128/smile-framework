package org.smile.ioc.bean;

import org.smile.config.BeanCreateException;
/**
 * 
 * @author 胡真山
 *
 * @param <T>
 */
public interface  FactoryBean<T> {
	/**
	 * 生成bean对象
	 * @return
	 * @throws BeanCreateException
	 */
	public T getObject() throws BeanCreateException;
	/**
	 * bean对象类型
	 * @return
	 */
	public Class<T> getObjectType();
	/**
	 * 是否是单例
	 * @return
	 */
	public boolean isSingleton();
	/**
	 * 注入到factory时执行操作
	 */
	public void onRegsitToFactory();
	/**
	 * 设置是否是单例
	 * @param single
	 */
	void setSingleton(boolean single);
}
