package org.smile.ioc.bean;

import java.util.ArrayList;
import java.util.List;

import org.smile.beans.converter.BeanException;
import org.smile.ioc.BeanFactory;
import org.smile.plugin.Interceptor;

public abstract class AbstractBeanFactory implements BeanFactory{
	/**拦截器*/
	protected List<Interceptor> interceptors=new ArrayList<Interceptor>();
	/**
	 * 所有的拦截器
	 * @return
	 */
	public List<Interceptor> getInterceptors() {
		return interceptors;
	}

	/**
	 * 注册拦截器
	 * @param interceptor
	 */
	public synchronized void  registInterceptor(Interceptor interceptor){
		interceptors.add(interceptor);
	}
	
	@Override
	public <T> T getBean(String id) throws BeanException {
		return getBean(id, true);
	}
}
