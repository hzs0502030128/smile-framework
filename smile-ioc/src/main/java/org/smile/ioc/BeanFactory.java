package org.smile.ioc;

import java.util.List;

import org.smile.beans.BeanFactorySupport;
import org.smile.beans.converter.BeanException;
import org.smile.ioc.bean.BeanBuilder;
import org.smile.ioc.load.FactoryBeanRegistry;
import org.smile.plugin.Interceptor;

public interface BeanFactory extends BeanFactorySupport<BeanBuilder> {
	/**
	 * 所有的拦截器
	 * 
	 * @return
	 */
	public List<Interceptor> getInterceptors();

	/**
	 * 注册拦截器
	 * 
	 * @param proxy
	 */
	public void registInterceptor(Interceptor proxy);

	/***
	 * 在加载后执行
	 * 
	 * @throws BeanException
	 */
	public abstract void afterLoad() throws BeanException;
	
	public void onBeanRegistry() throws BeanException;
}
