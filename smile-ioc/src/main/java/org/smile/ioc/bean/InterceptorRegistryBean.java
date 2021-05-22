package org.smile.ioc.bean;

import java.util.LinkedList;
import java.util.List;

import org.smile.beans.converter.BeanException;
import org.smile.ioc.BeanFactory;
import org.smile.ioc.load.FactoryBeanRegistry;
import org.smile.plugin.ClassRegExpInterceptor;
import org.smile.plugin.Interceptor;
/**
 *      可以往beanfactory中注入拦截器
 * @author 胡真山
 *
 */
public class InterceptorRegistryBean extends ClassRegExpInterceptor implements FactoryBeanRegistry{
	/**要注入factory中的拦截器*/
	private List<Interceptor> interceptors=new LinkedList<Interceptor>();
	
	@Override
	public void processBeanRegistry(BeanFactory beanFactory) throws BeanException {
		for(Interceptor i:interceptors){
			beanFactory.registInterceptor(i);
		}
	}

	public void setInterceptors(List<Interceptor> interceptors) {
		this.interceptors = interceptors;
	}

}
