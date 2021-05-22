package org.smile.ioc.load;

import org.smile.beans.converter.BeanException;
import org.smile.ioc.BeanFactory;
/**
 * 可以往beanfactory中注入对象的接口
 * @author 胡真山
 *
 */
public interface FactoryBeanRegistry {
	
	public void processBeanRegistry(BeanFactory beanFactory) throws BeanException;
}
