package org.smile.orm.load;

import org.smile.orm.DaoFactory;


public interface IApplicationConfig {
	/**添加一个应用*/
	public void addApplication(String id,Application app);
	/**以id获取应用*/
	public Application getApplication(String id);
	/**获取默认应用*/
	public Application getApplication();
	/**获取默认应用中的Dao实例*/
	public <E> E getDaoBean(Class<E> interfaceDao);
	/**以应用id获取Dao工厂实例*/
	public DaoFactory getDaoFactory(String id);
}
