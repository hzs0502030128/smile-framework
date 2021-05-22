package org.smile.ioc.bean;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.smile.beans.converter.BeanException;
import org.smile.config.BeanCreateException;
import org.smile.ioc.IocInitException;
import org.smile.ioc.load.FactoryBeanRegistry;
import org.smile.reflect.ClassTypeUtils;

public class DefaultBeanFactory extends AbstractBeanFactory {
	/** 以映射的bean创建者 */
	private Map<String, BeanBuilder> idCreatorMap = new ConcurrentHashMap<String, BeanBuilder>();
	/** 以类名映射的bean创建者 */
	private Map<Class, BeanBuilder> classCreatorMap = new ConcurrentHashMap<Class, BeanBuilder>();
	
	private Map<String,BeanBuilder> idAndClassNameCreatorMap=new ConcurrentHashMap<String, BeanBuilder>();


	@Override
	public void afterLoad() throws BeanException {
		for (BeanBuilder builder : idCreatorMap.values()) {
			builder.afterLoad();
		}
	}

	@Override
	public <T> T getBean(Class<T> beanClass) throws BeanException {
		BeanBuilder creator = classCreatorMap.get(beanClass);
		if (creator == null) {
			throw new BeanCreateException("不存在 class 为 " + beanClass + " 的配置");
		}
		return (T) creator.getBean();
	}

	@Override
	public synchronized void registProducer(String id, BeanBuilder creator) {
		if (idCreatorMap.containsKey(id)) {
			throw new IocInitException("重复定义的 bean id:" + id + "-->" + creator);
		}
		idCreatorMap.put(id, creator);
		idAndClassNameCreatorMap.put(id, creator);
		registProducerByClass(creator);
		Class[] interfaces = ClassTypeUtils.getAllInterfaces(creator.getBeanClass());
		for (Class clazz : interfaces) {
			classCreatorMap.put(clazz, creator);
			idAndClassNameCreatorMap.put(clazz.getName(), creator);
		}
	}
	/**
	 * 以类名注入
	 * @param creator
	 */
	private void registProducerByClass(BeanBuilder creator) {
		idAndClassNameCreatorMap.put(creator.getBeanClass().getName(), creator);
		classCreatorMap.put(creator.getBeanClass(), creator);
	}

	@Override
	public <T> T getBean(String id, boolean notExsitsError) throws BeanException {
		BeanBuilder creator = idAndClassNameCreatorMap.get(id);
		if (creator == null) {
			if(notExsitsError) {
				throw new BeanCreateException("不存在id为" + id + "的配置");
			}
			return null;
		}
		return (T)creator.getBean();
	}

	@Override
	public void onBeanRegistry() throws BeanException {
		for (BeanBuilder builder : idCreatorMap.values()) {
			Class clazz = builder.getBeanClass();
			if (FactoryBeanRegistry.class.isAssignableFrom(clazz)) {
				FactoryBeanRegistry bean = (FactoryBeanRegistry) builder.getBean();
				bean.processBeanRegistry(this);
			}
		}
	}

}
