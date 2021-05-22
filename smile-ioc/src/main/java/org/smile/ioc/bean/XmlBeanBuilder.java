package org.smile.ioc.bean;

import java.util.List;

import org.smile.beans.converter.BeanException;
import org.smile.config.BeanConfig;
import org.smile.config.BeanCreateException;
import org.smile.config.BeanCreator;
import org.smile.config.parser.TagConfigParser;
import org.smile.ioc.BeanFactory;
import org.smile.ioc.aware.AwareHandler;
import org.smile.plugin.Interceptor;
/**
 * 使用xml文件配置的bean生成器
 * @author 胡真山
 * @date 2018年5月21日
 *
 */
public class XmlBeanBuilder extends BeanCreator implements BeanBuilder{
	/**注册到的工厂*/
	private BeanFactory factory;
	
	private XmlBuildFactoryBean factoryBean;
	
	public XmlBeanBuilder(BeanConfig config) throws BeanCreateException {
		super(config);
		this.factoryBean=new XmlBuildFactoryBean();
		this.factoryBean.setType(this.clazz);
		this.factoryBean.setSingleton(config.isSingle());
	}
	/**
	 * 设置生成器工厂
	 * @param factory
	 */
	public void setFactory(BeanFactory factory) {
		this.factory = factory;
		this.parser=new TagConfigParser(factory);
	}

	@Override
	protected Object createBean() throws BeanException {
		Object bean= factoryBean.getObject();
		AwareHandler.handler(bean,getBeanId());
		return bean;
	}
	
	@Override
	protected Object getSingleton() throws BeanException {
		if(singleton==null){
			synchronized(this) {
				if(singleton==null) {
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
	@Override
	public void regsitToFactory(BeanFactory factory) throws BeanCreateException {
		try {
			this.setFactory(factory);
			factory.registProducer(getBeanId(), this);
			this.factoryBean.setFactory(this.factory);
			factoryBean.onRegsitToFactory();
		} catch (Exception e) {
			throw new BeanCreateException("创建自定义bean失败" + getBeanClass() + " id " + getBeanId(), e);
		}
	}

	@Override
	public void afterLoad() throws BeanException {
		getSingleton();
	}

	@Override
	public Class getBeanClass() {
		return clazz;
	}
	
	class XmlBuildFactoryBean extends DefaultFactoryBean<Object>{

		@Override
		protected Object newInstance() throws BeanException {
			return XmlBeanBuilder.super.createBean();
		}
	}
	
}
