package org.smile.beans;

import org.smile.beans.converter.BeanException;

public abstract class AbstractBeanProducer implements BeanProducer{
	
	/**用于保存创建bean的单例*/
	protected volatile Object singleton;
	/**
	 * 获取创建的bean对象
	 * @return
	 * @throws BeanException
	 */
	@Override
	public Object getBean() throws BeanException{
		if(isSingle()){
			return getSingleton();
		}
		return createBean();
	}
	/**
	 * 是否是单例模式
	 * @return
	 */
	protected abstract boolean isSingle();
	/**
	 * 获取单例
	 * @return
	 * @throws BeanException
	 */
	protected Object getSingleton()  throws BeanException{
		if(singleton==null){
			singleton=createBean();
		}
		return singleton;
	}

	/**
	 * 新创建bean对象
	 * @return
	 */
	protected abstract Object createBean() throws BeanException;
	
}
