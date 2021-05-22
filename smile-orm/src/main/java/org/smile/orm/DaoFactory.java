package org.smile.orm;

import java.util.Collection;

import org.smile.collection.FastHashMap;
import org.smile.orm.dao.DaoMapper;
import org.smile.orm.dao.MapperProxy;
/**
 * Dao工厂类
 * @author 胡真山
 *
 */
public class DaoFactory {
	
	private OrmApplication application;
	/**
	 * dao代理实例集合
	 */
	private FastHashMap<Class,MapperProxy> mapperProxys=new FastHashMap<Class,MapperProxy>();
	/**
	 * 构建一个Dao工厂从orm应用程序
	 * @param application
	 */
	public DaoFactory(OrmApplication application){
		this.application=application;
		initFactory();
	}
	/**
	 * 初始化工厂
	 */
	private void initFactory(){
		Collection<DaoMapper> daoList=application.getAllMapper();
		for(DaoMapper mapper:daoList){
			MapperProxy proxy=new MapperProxy(application, mapper);
			mapperProxys.put(mapper.getDaoClass(), proxy);
		}
		mapperProxys.toFasted();
	}
	/**
	 * 获取Dao实例
	 * @param interfaceDao dao接口类
	 * @return
	 */
	public <E> E getDaoBean(Class<E> interfaceDao){
		MapperProxy<E> proxy=mapperProxys.get(interfaceDao);
		if(proxy!=null&&proxy.isSingle()){
			return proxy.getInterfaceDao();
		}
		return new MapperProxy<E>(application,interfaceDao).getInterfaceDao();
	}
	
	public OrmApplication getOrmApplication() {
		return this.application;
	}
}
