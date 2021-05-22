package org.smile.ioc.bean;

import java.lang.reflect.Method;
import java.util.List;

import org.smile.annotation.AnnotationUtils;
import org.smile.beans.AbstractBeanProducer;
import org.smile.beans.converter.BeanException;
import org.smile.collection.ArrayUtils;
import org.smile.commons.ann.Bean;
import org.smile.commons.ann.Sington;
import org.smile.config.BeanCreateException;
import org.smile.ioc.BeanFactory;
import org.smile.ioc.aware.AwareHandler;
import org.smile.plugin.Interceptor;
import org.smile.reflect.MethodParamNameUtils;
import org.smile.util.StringUtils;

public class ConfigBeanBuilder extends AbstractBeanProducer implements BeanBuilder{

	private Object configObject;
	private BeanBuilder sourceBeanBuilder;
	private Method createMethod;
	private String beanId;
	private BeanFactory beanFactory;
	private ConfigFactoryBean factoryBean=new ConfigFactoryBean();
	
	public ConfigBeanBuilder(BeanBuilder sourceBeanBuilder,Method createMethod,Bean bean) {
		this.sourceBeanBuilder=sourceBeanBuilder;
		this.createMethod=createMethod;
		Sington singtonAnn=AnnotationUtils.getAnnotation(createMethod, Sington.class);
		if(singtonAnn!=null) {
			this.factoryBean.setSingleton(singtonAnn.value());
		}
		this.beanId=bean.value();
		if(StringUtils.isEmpty(this.beanId)) {//注解没有配置bean名称时使用方法名称
			this.beanId=this.createMethod.getName();
		}
		Class beanType=createMethod.getReturnType();
		this.factoryBean.setType(beanType);
	}
	
	@Override
	public Class getBeanClass() {
		return factoryBean.type;
	}

	@Override
	public String getBeanId() {
		return beanId;
	}

	@Override
	protected boolean isSingle() {
		return this.factoryBean.isSingleton();
	}
	
	
	class ConfigFactoryBean extends DefaultFactoryBean<Object>{
		
		@Override
		protected Object newInstance() throws BeanException {
			Object[] argsValue=null;
			Class[] argsType=createMethod.getParameterTypes();
			if(ArrayUtils.notEmpty(argsType)) {
				argsValue=new Object[argsType.length];
				String[] argsName=MethodParamNameUtils.getParamNames(createMethod);
				for(int i=0;i<argsType.length;i++) {
					Object param=null;
					if(ArrayUtils.notEmpty(argsName)) {
						if(argsName.length>i) {
							param=this.factory.getBean(argsName[i],false);
						}
					}
					if(param==null) {
						param=this.factory.getBean(argsType[i]);
					}
					argsValue[i]=param;
				}
			}
			try {
				return createMethod.invoke(configObject, argsValue);
			} catch (Exception e) {
				throw new BeanException(e);
			}
		}
		
	}

	@Override
	public void afterLoad() throws BeanException {
		getSingleton();
	}

	@Override
	public void regsitToFactory(BeanFactory factory) throws BeanCreateException {
		try {
			this.beanFactory=factory;
			factory.registProducer(getBeanId(), this);
			this.factoryBean.setFactory(factory);
			factoryBean.onRegsitToFactory();
		} catch (Exception e) {
			throw new BeanCreateException("创建自定义bean失败" + getBeanClass() + " id " + getBeanId(), e);
		}
	}

	@Override
	protected Object getSingleton() throws BeanException {
		if(singleton==null){
			synchronized(this) {
				if(singleton==null) {
					Object instance= createBean();
					List<Interceptor> intercepters=this.beanFactory.getInterceptors();
					int index=intercepters.size()-1;
					for (;index>=0;index--) {
						instance = intercepters.get(index).plugin(instance);
					}
					this.singleton=instance;
				}
			}
		}
		return singleton;
	}

	@Override
	protected Object createBean() throws BeanException {
		synchronized(this) {
			if(this.configObject==null) {
				this.configObject=this.sourceBeanBuilder.getBean();
			}
		}
		Object bean = factoryBean.getObject();
		AwareHandler.handler(bean, getBeanId());
		return bean;
	}

}
