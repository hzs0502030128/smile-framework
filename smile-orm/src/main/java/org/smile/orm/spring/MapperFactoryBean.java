package org.smile.orm.spring;

import org.smile.orm.dao.MapperProxy;
import org.springframework.beans.factory.FactoryBean;

public class MapperFactoryBean<T> implements FactoryBean<T>{

	private MapperProxy<T> proxy; 
	
	private Class<T> type;
	
	private boolean singleton;
	@Override
	public T getObject() throws Exception {
		if(singleton){
			return proxy.getInterfaceDao();
		}else{
			return MapperProxy.copy(proxy).getInterfaceDao();
		}
	}

	@Override
	public Class<T> getObjectType() {
		return type;
	}

	@Override
	public boolean isSingleton() {
		return singleton;
	}
	
	public void setSingleton(boolean singleton) {
		this.singleton = singleton;
	}

	public void setProxy(MapperProxy<T> proxy){
		this.proxy=proxy;
	}

	public void setType(Class<T> type) {
		this.type = type;
	}

}
