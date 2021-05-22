package org.smile.orm.dao;

import org.smile.db.DataSourceSupport;
import org.smile.orm.OrmInitException;

public class BaseDaoTargetHandler implements DaoTargetHandler {
	@Override
	public Object getTarget(String targetClass,Executor executor) {
		try{
			Object daoTarget=Class.forName(targetClass).newInstance();
			/**设置datasource*/
			if(daoTarget instanceof DataSourceSupport){
				((DataSourceSupport)daoTarget).setDataSource(executor.getDataSource());
			}
			return daoTarget;
		}catch(Exception e){
			throw new OrmInitException("初始化daoTarget出错",e);
		}
	}

}
