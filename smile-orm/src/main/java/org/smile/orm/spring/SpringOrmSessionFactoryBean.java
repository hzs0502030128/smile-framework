package org.smile.orm.spring;

import org.smile.db.spring.SpringTransactionHandler;
import org.smile.orm.dao.DaoMapper;
import org.smile.orm.dao.MapperProxy;
import org.smile.orm.load.ClassPathXmlApplication;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringOrmSessionFactoryBean extends ClassPathXmlApplication implements BeanDefinitionRegistryPostProcessor, ApplicationContextAware {

	private DefaultListableBeanFactory acf;

	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefin) throws BeansException {
		initOrmApplication();
		getOrmApplication().setDaoTargetHandler(new SpringDaoTargetHandler(acf));
		getOrmApplication().getExecutor().setTransactionHandler(new SpringTransactionHandler());
		BeanDefinitionBuilder bdb;
		for (DaoMapper<?> mapper : application.getAllMapper()) {
			bdb = BeanDefinitionBuilder.rootBeanDefinition(MapperFactoryBean.class.getName());
			bdb.getBeanDefinition().setAttribute("id", mapper.getName());
			bdb.addPropertyValue("proxy", new MapperProxy<>(application, mapper));
			bdb.addPropertyValue("type", mapper.getDaoClass());
			bdb.addPropertyValue("singleton", mapper.isSingle());
			// 注册bean
			acf.registerBeanDefinition(mapper.getName(), bdb.getBeanDefinition());
		}
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory factory) throws BeansException {
		//
	}

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		this.acf = (DefaultListableBeanFactory) context.getAutowireCapableBeanFactory();
	}

}
