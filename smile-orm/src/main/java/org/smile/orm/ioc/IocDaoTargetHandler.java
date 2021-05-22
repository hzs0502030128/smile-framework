package org.smile.orm.ioc;

import org.smile.beans.converter.BeanException;
import org.smile.ioc.BeanFactory;
import org.smile.orm.OrmInitException;
import org.smile.orm.dao.BaseDaoTargetHandler;
import org.smile.orm.dao.DaoTargetHandler;
import org.smile.orm.dao.Executor;

public class IocDaoTargetHandler extends BaseDaoTargetHandler implements DaoTargetHandler {
	
	private BeanFactory factory;
	
	public IocDaoTargetHandler(BeanFactory factory){
		this.factory=factory;
	}
	
	@Override
	public Object getTarget(String targetClass, Executor executor) {
		try {
			return factory.getBean(targetClass);
		} catch (BeanException e) {
			throw new OrmInitException("初始化daoTarget失败"+targetClass, e);
		}
	}

}
