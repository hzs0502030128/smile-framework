package org.smile.ioc.bean;

import org.smile.beans.converter.BeanException;
import org.smile.config.BeanCreateException;
import org.smile.reflect.ClassTypeUtils;

public abstract class AbstractFactoryBean<T> implements FactoryBean<T> {

	/***是否是使用单例*/
	protected boolean singleton=true;
	/** 生成bean的类型*/
	protected Class<T> type;
	/**单例保存*/
	protected T singleObject;

	@Override
	public T getObject() throws BeanCreateException {
		if (singleton) {
			if (singleObject == null) {
				singleObject = createObject();
			}
			return singleObject;
		} else {
			return createObject();
		}
	}
	/**
	 * 创建新对旬
	 * @return
	 * @throws BeanCreateException
	 */
	protected abstract T createObject() throws BeanCreateException;

	@Override
	public Class<T> getObjectType() {
		return type;
	}

	@Override
	public boolean isSingleton() {
		return singleton;
	}
	/**
	 * 设置bean对象类型
	 * @param type
	 */
	public void setType(Class<T> type) {
		this.type = type;
	}
	/**
	 * 设置是否是单例
	 * @param single
	 */
	@Override
	public void setSingleton(boolean single) {
		this.singleton = single;
	}
	
	/**
	 * 	实例化新对象 
	 * 	创建新实例时调用
	 * @return
	 * @throws BeanException
	 */
	protected T newInstance() throws BeanException {
		T bean=ClassTypeUtils.newInstance(type);
		return bean;
	}
}
