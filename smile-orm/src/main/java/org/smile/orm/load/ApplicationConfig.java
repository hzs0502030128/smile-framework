package org.smile.orm.load;

import java.util.Map;

import org.smile.collection.FastHashMap;
import org.smile.orm.DaoFactory;
import org.smile.orm.OrmApplication;

public class ApplicationConfig implements IApplicationConfig{
	
	public static final  String DEFAULT="default";
	/**多个应用的集合*/
	protected Map<String,Application> applications=new FastHashMap<String,Application>(true);
	/**多个dao工厂*/
	protected Map<String,DaoFactory> daoFactory=new FastHashMap<String, DaoFactory>(true);
	/**默认的工厂*/
	protected DaoFactory defaultDaoFactory;
	
	@Override
	public void addApplication(String id,Application app){
		this.applications.put(id, app);
		DaoFactory factory=new DaoFactory(app.getOrmApplication());
		daoFactory.put(id, factory);
		if(DEFAULT.equals(id)){
			this.defaultDaoFactory=factory;
		}
	}
	
	@Override
	public Application getApplication(String id){
		return applications.get(id);
	}
	
	@Override
	public Application getApplication(){
		return applications.get(DEFAULT);
	}
	@Override
	public <E> E getDaoBean(Class<E> interfaceDao){
		return defaultDaoFactory.getDaoBean(interfaceDao);
	}

	@Override
	public DaoFactory getDaoFactory(String id) {
		return daoFactory.get(id);
	}
	/**
	 * 	默认为 ormapplication
	 * @return
	 */
	public OrmApplication getOrmApplication() {
		return this.defaultDaoFactory.getOrmApplication();
	}
}
