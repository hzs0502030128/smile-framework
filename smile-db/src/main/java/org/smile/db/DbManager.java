package org.smile.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.smile.Smile;
import org.smile.db.config.ConfigContext;
import org.smile.db.config.DataSourceConfig;
import org.smile.db.config.JdbcConfig;
import org.smile.db.pool.DataSourceContext;
import org.smile.log.LoggerHandler;

/**
 * 数据库连接工具管理
 * 
 * @author 胡真山
 *
 */
public class DbManager implements LoggerHandler {
	/**
	 */
	private static Context context = null;
	/**
	 * 默认连接数据源名称
	 */
	private static String default_datasource;
	/**
	 * 连接池数据源信息
	 */
	private static Map<String, DataSource> dsMap = new ConcurrentHashMap<String, DataSource>();

	static {
		try {
			init();
		} catch (Throwable e) {
			logger.error("DbManager 初始化失败", e);
		}
	}

	/**
	 * 清除所有的数据库连接信息
	 * 
	 * @throws SQLException
	 */
	public static void clear() throws SQLException {
		default_datasource = null;
		try {
			DataSourceContext.unbindAll();
		} catch (NameNotFoundException e) {
			throw new SQLException("unbind datasource error",e);
		}
		dsMap.clear();
	}

	/**
	 * 重新计取配置文件，重置管理器
	 */
	public static void reset() throws SQLException{
		try {
			clear();
			DataSourceContext.rebindAll();
		} catch (Exception e) {
			throw new SQLException("重置DbManager出错", e);
		}
		init();
	}

	/**
	 * 初始化
	 */
	private static void init() {
		try {
			context = new InitialContext();
		} catch (Throwable e) {
			logger.error(e);
		}
		for (DataSourceConfig config : ConfigContext.getInstance().getAllDataSourceConfig()) {
			if (config.isDefault()) {
				default_datasource = config.getName();
			}
			// 加入到DbManager中
			try {
				DataSource ds = getDataSource(config.getRef());
				if (ds != null) {
					dsMap.put(config.getName(), ds);
					logger.info("加载数据源配置[name:" + config.getName() + ",ref:" + config.getRef() + "]成功");
				} else {
					logger.info("加载数据源配置[name:" + config.getName() + ",ref:" + config.getRef() + "]失败");
				}
			} catch (Exception e) {
				logger.error("初始化数据源出错", e);
			}
		}
	}

	/**
	 * 获取已经被DbManager管理的所有的数据源
	 * 
	 * @return
	 */
	public static Map<String, DataSource> getManagedDataSources() {
		return dsMap;
	}

	/**
	 * 获取已经被DbManager管理的所有的数据源
	 * 
	 * @param name system_config中配置的名称
	 * @return
	 */
	public static DataSource getManagedDataSource(String name) {
		return dsMap.get(name);
	}

	/***
	 * 获取一个在配置文件中配置了jdbc信息的连接
	 * 
	 * @param name 配置文件中jdbc 中的name
	 * @return 数据库连接
	 * @throws SQLException 
	 */
	public static Connection getJdbcConnection(String name) throws SQLException {
		try {
			JdbcConfig config = ConfigContext.getInstance().getJdbcConfig(name);
			if (config == null) {
				throw new SQLException("没有配置一个名称为[" + name + "]的JDBC连接信息");
			} else {
				return getJdbcConnection(config.getDriver(), config.getUrl(), config.getUsername(), config.getPassword());
			}
		} catch (Exception e) {
			throw new SQLException("JDBC连接数据库出错 - name-> "+name, e);
		}
	}

	/**
	 * 获取数据库连接
	 * 
	 * @param driverName
	 * @param url
	 * @param userName
	 * @param password
	 * @return
	 * @throws SQLException 
	 */
	public static Connection getJdbcConnection(String driverName, String url, String userName, String password) throws SQLException {
		try {
			Class.forName(driverName);
			Connection conn = DriverManager.getConnection(url, userName, password);
			return conn;
		} catch (Exception e) {
			throw new SQLException("连接数据库[url：" + url + "]出现错误", e);
		}
	}

	/** 开启一个jdbc连接 
	 * @throws SQLException 
	 **/
	public static Connection getJdbcConnection(JdbcConfig config) throws SQLException {
		return getJdbcConnection(config.getDriver(), config.getUrl(), config.getUsername(), config.getPassword());
	}

	/**
	 * 从一个配置文件读取信息 获得一个连接
	 * 
	 * @param configFile
	 * @return
	 * @throws SQLException 
	 */
	public static Connection getJdbcConnFormBundle(String configFile) throws SQLException {
		ResourceBundle dbResources = ResourceBundle.getBundle(configFile);
		String driver = dbResources.getString("driver");
		String url = dbResources.getString("url");
		String user = dbResources.getString("user");
		String password = dbResources.getString("password");
		return getJdbcConnection(driver, url, user, password);
	}

	/**
	 * 得到smile 数据源
	 * 
	 * @param _jndi 数据源名称
	 * @return 数据源
	 * @throws SQLException 
	 */
	public static DataSource getSmileDataSource(String _jndi) throws SQLException {
		// 从smile实现的数据源中配置文件中初始化
		try {
			return DataSourceContext.lookup(_jndi);
		} catch (NameNotFoundException e) {
			throw new SQLException("no datasource find from smile datasource context named " + _jndi + " …………");
		}
	}

	/**
	 * 以数据源的名称查询数据源 此名称不是DbManager中管理的名称 而是 真实的数据源容器中的名称 如：tomcat中配置的名称 他的查询顺序为
	 * ： 1、从 system_config.xml 中 查询<resource
	 * />标签配置的数据源,即org.huzs.db.pool.BasicDataSource实现的数据源 2、如果没有则会
	 * 以["java:comp/env/"+_jndi]这样的规则去InitialContext中查找 3、如果还是没有
	 * 则会以_jndi为名称去InitialContext中查找，因为有的服务器中的连接池规则是不需要java:comp/env/做为前缀的
	 * 此方法不推荐手动调用 建设在system_config中配置一个数据源参数
	 * 
	 * @return
	 * @throws NamingException
	 */
	public static DataSource getDataSource(String _jndi) throws NamingException {
		DataSource ds = dsMap.get(_jndi);
		if (ds == null) {
			// 从smile实现的数据源中配置文件中初始化
			try {
				ds = DataSourceContext.lookup(_jndi);
			} catch (Exception e) {
				// 服务器中查找
				try {
					ds = (DataSource) context.lookup("java:comp/env/" + _jndi);
				} catch (Exception ee) {
					try {
						ds = (DataSource) context.lookup(_jndi);
					} catch (Exception eee) {
						try {
							ds = (DataSource) context.lookup("java:comp/env/jdbc/" + _jndi);
						} catch (Exception eeee) {
							logger.error("不能获取名称为" + _jndi + " 的数据源信息: " + eee.getMessage());
						}
					}
				}
			}
			if (ds != null) {
				dsMap.put(_jndi, ds);
			}
		}
		return ds;
	}

	/**
	 * 得到默认的数据库连接
	 * 
	 * @return
	 * @throws SQLException 
	 */
	public static Connection getConnection() throws SQLException {
		try {
			if (default_datasource == null) {
				throw new SQLException("你没有配置默认的数据源连接,请在" + Smile.DB_FILE_NAME + "文件中指定");
			} else {
				return dsMap.get(default_datasource).getConnection();
			}
		} catch (NullPointerException e) {
			throw new SQLException("连接数据库失败,从被管理的默认数据源[" + default_datasource + "]", e);
		}
	}

	/**
	 * 是否有默认数据源配置
	 * 
	 * @return
	 */
	public static boolean hasDefaultDataSource() {
		return default_datasource != null;
	}

	/**
	 * 如果配置了默认的数据源，则反回默认的数据源
	 * 
	 * @return
	 * @throws NamingException
	 */
	public static DataSource getDefaultDataSource() throws NamingException {
		return getDataSource(default_datasource);
	}

	/**
	 * 指这一个数据源名称得到数据库连接
	 * 
	 * @param jndi
	 * @return
	 * @throws NamingException 
	 * @throws SQLException 
	 */
	public static Connection getConnection(String name) throws SQLException{
		Connection conn = null;
		DataSource ds = dsMap.get(name);
		if (ds != null) {
			conn = ds.getConnection();
		} else {
			try {
				conn = getDataSource(name).getConnection();
			} catch (NamingException e) {
				throw new SQLException("not datasource config named "+name, e);
			}
		}
		return conn;
	}
	/**
	 * 设置默认的数据源名称
	 * @param name
	 */
	public static void registDataSource(String name,DataSource ds,boolean isdefault){
		dsMap.put(name, ds);
		if(isdefault){
			default_datasource=name;
		}
	}
	
}
