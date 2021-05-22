package org.smile.orm.spring;

import org.smile.orm.dao.DaoTargetHandler;
import org.smile.orm.dao.Executor;
import org.springframework.beans.factory.BeanFactory;

public class SpringDaoTargetHandler implements DaoTargetHandler {
	
	private BeanFactory factory;
	
	public SpringDaoTargetHandler(BeanFactory factory){
		this.factory=factory;
	}
	
	@Override
	public Object getTarget(String targetClass, Executor executor) {
		return factory.getBean(targetClass);
	}

}
