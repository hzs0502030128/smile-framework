package org.smile.ioc;

import java.io.InputStream;

import org.smile.beans.converter.BeanException;
import org.smile.ioc.bean.DefaultBeanFactory;
import org.smile.ioc.load.IocContextLoader;

public class ClassPathIocContext implements IocContext{
	
	protected BeanFactory beanFactory; 
	
	public ClassPathIocContext(String config){
		this.beanFactory=new DefaultBeanFactory();
		new IocContextLoader().loadClassXmlConfig(this,config);
		afterLoad();
	}
	
	protected ClassPathIocContext(){
		this.beanFactory=new DefaultBeanFactory();
	}
	
	public ClassPathIocContext(InputStream is) {
		this.beanFactory=new DefaultBeanFactory();
		new IocContextLoader().loadClassXmlConfig(this,is);
		afterLoad();
	}
	
	@Override
	public void afterLoad(){
		try {
			beanFactory.onBeanRegistry();
			beanFactory.afterLoad();
		} catch (BeanException e) {
			throw new IocInitException(e);
		}
	}
	
	public Object getBean(String id) throws BeanException{
		return beanFactory.getBean(id);
	}
	

	@Override
	public BeanFactory getBeanFactory() {
		return beanFactory;
	}

	@Override
	public void distory() {
		
	}

	@Override
	public <T> T getBean(Class<T> beanClass) throws BeanException {
		return beanFactory.getBean(beanClass);
	}
}
