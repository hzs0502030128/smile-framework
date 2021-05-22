package org.smile.orm.ioc;

import org.smile.beans.converter.BeanException;
import org.smile.ioc.BeanFactory;
import org.smile.ioc.bean.BeanDefinition;
import org.smile.ioc.load.FactoryBeanRegistry;
import org.smile.orm.dao.DaoMapper;
import org.smile.orm.dao.MapperProxy;
import org.smile.orm.load.ClassPathXmlApplication;
import org.smile.transaction.SmileTransactionHandler;


public class IocOrmSessionFactoryBean extends  ClassPathXmlApplication implements FactoryBeanRegistry{
	
	@Override
	public void processBeanRegistry(BeanFactory beanFactory) throws BeanException{
		initOrmApplication();
		getOrmApplication().setDaoTargetHandler(new IocDaoTargetHandler(beanFactory));
		getOrmApplication().getExecutor().setTransactionHandler(new SmileTransactionHandler());
		BeanDefinition beanDef;  
		for(DaoMapper<?> mapper:application.getAllMapper()){
			beanDef=new BeanDefinition();  
			beanDef.getAttribute().setId(mapper.getName());  
			beanDef.getAttribute().setClazz(MapperFactoryBean.class);
			beanDef.getAttribute().setSingleton(mapper.isSingle());
			beanDef.addPropertyValue("proxy", new MapperProxy(application, mapper));
			beanDef.addPropertyValue("type", mapper.getDaoClass());
	        beanDef.regsitToFactory(beanFactory); 
		}
	}
}
