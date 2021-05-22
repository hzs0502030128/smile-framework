package org.smile.ioc.bean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.smile.beans.AbstractBeanProducer;
import org.smile.beans.BeanProperties;
import org.smile.beans.converter.BeanException;
import org.smile.config.BeanCreateException;
import org.smile.ioc.BeanFactory;
import org.smile.ioc.aware.AwareHandler;
import org.smile.plugin.Interceptor;
import org.smile.reflect.ClassTypeUtils;

/**
 * bean的定义类
 * 
 * @author 胡真山
 * @Date 2016年5月9日
 */
public class BeanDefinition extends AbstractBeanProducer implements BeanBuilder {
	/** 定义的属性信息 */
	private BeanAttribute attribute = new BeanAttribute();
	/** 设置属性的值 */
	private Map<String, Object> propertyValue = new HashMap<String, Object>();
	/** 参考的值设置 */
	private Map<String, String> refValue = new HashMap<String, String>();
	
	private BeanProperties properties= BeanProperties.NORAL;
	/** 生成器注册的工厂 */
	private BeanFactory factory;
	/** 工厂bean定义类 */
	private FactoryBean<Object> factoryBean;

	/**
	 * 设置属性的值
	 * 
	 * @param name
	 * @param value
	 */
	public void addPropertyValue(String name, Object value) {
		propertyValue.put(name, value);
	}

	public void addRefValue(String name, String value) {
		refValue.put(name, value);
	}

	/**
	 * bean通用基本属性
	 * 
	 * @return
	 */
	public BeanAttribute getAttribute() {
		return attribute;
	}

	@Override
	public Object createBean() throws BeanCreateException {
		Object bean = factoryBean.getObject();
		AwareHandler.handler(bean, attribute.getId());
		return bean;
	}
	
	@Override
	public Object getSingleton() throws BeanCreateException {
		if (singleton == null) {
			synchronized(this) {//初始化单例对象
				if(singleton==null) {
					initRefValue();
					Object instance= createBean();
					List<Interceptor> intercepters=factory.getInterceptors();
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

	/**
	 * 	注解此bean定义到beanfactory中
	 * 
	 * @param factory
	 * @throws BeanCreateException
	 */
	@Override
	public void regsitToFactory(BeanFactory factory) throws BeanCreateException {
		try {
			initPropertiesValue(factory);
			factory.registProducer(attribute.getId(), this);
			factoryBean.onRegsitToFactory();
		} catch (Exception e) {
			throw new BeanCreateException("创建自定义bean失败" + attribute.getClazz() + " id " + attribute.getId(), e);
		}
	}

	/***
	 * 初始化非参考的属性值
	 * 
	 * @throws Exception
	 */
	protected void initPropertiesValue(BeanFactory factory) throws BeanException {
		this.factory = factory;
		Class<? extends FactoryBean> factoryBeanClass=attribute.getClazz();
		factoryBean = ClassTypeUtils.newInstance(factoryBeanClass);
		factoryBean.setSingleton(attribute.isSingleton());
		for (Map.Entry<String, Object> entry : propertyValue.entrySet()) {
			properties.setFieldValue(entry.getKey(), entry.getValue(), factoryBean);
		}
	}

	/***
	 * 初始化参考的属性值
	 * 
	 * @throws BeanException
	 */
	protected void initRefValue() throws BeanCreateException {
		try {
			for (Map.Entry<String, String> entry : refValue.entrySet()) {
				Object value = factory.getBean(entry.getValue());
				if (value != null) {
					properties.setFieldValue(entry.getKey(), value, factoryBean);
				}
			}
		} catch (Exception e) {
			throw new BeanCreateException("初始化" + factoryBean + "的ref属性出错", e);
		}
	}

	@Override
	public void afterLoad() throws BeanException {
		getSingleton();
	}

	@Override
	public String toString() {
		return attribute.getId() + "-" + factoryBean.getObjectType() + "-" + super.toString();
	}

	@Override
	public Class getBeanClass() {
		return factoryBean.getObjectType();
	}

	@Override
	public String getBeanId() {
		return attribute.getId();
	}

	@Override
	protected boolean isSingle() {
		return factoryBean.isSingleton();
	}
}
