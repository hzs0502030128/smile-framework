package org.smile.db.pool;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.smile.db.config.ResourceConfig;
import org.smile.db.sql.BasicTransaction;
import org.smile.db.sql.SQLRunner;
import org.smile.db.sql.SqlExecutor;
import org.smile.json.JSON;
import org.smile.util.StringUtils;

/**
 * 一个基本的数据源实现类
 * 
 * @author strive
 */
public class Pool2DataSource extends AbstractDataSource{
	/**
	 * 数据源连接池
	 */
	protected GenericObjectPool<PoolConnection> connectionPool;
	/**
	 * 构造一个数据数据源 在不使用此数据源，即把此数据源从连接池中删除必须关闭此数据源
	 * 
	 * @param param
	 * @throws SQLException
	 */
	public Pool2DataSource(ResourceConfig param) throws SQLException {
		this.param = param;
		//初始化一个连接
		PooledObjectFactory<PoolConnection> factory = new ConnPooledObjectFactory();
		GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
		poolConfig.setMaxIdle(param.getMinIdle());
		poolConfig.setMaxIdle(param.getMaxIdle());
		poolConfig.setMaxWaitMillis(param.getMaxWaitTime());
		poolConfig.setMaxTotal(param.getMaxActive());
		poolConfig.setTestOnBorrow(true);
		poolConfig.setMinEvictableIdleTimeMillis(param.getTimeOutValue());
		connectionPool= new GenericObjectPool<PoolConnection>(factory,poolConfig);
		try {
			//测试连接
			newPoolConnection(param,true).conn.close();
			logger.debug(JSON.toJSONString(poolConfig));
			connectionPool.preparePool();
		} catch (Exception e) {
			throw new SQLException("初始化连接池失败",e);
		}
	}

	/**
	 * 得到连接数
	 * 
	 * @return
	 */
	public  int getAllCount() {
		return connectionPool.getNumActive()+connectionPool.getNumIdle();
	}

	/**
	 * 得到空闲连接个数
	 * 
	 * @return
	 */
	public  int getFreeCount() {
		return connectionPool.getNumIdle();
	}

	/**
	 * 创建一个新的连接
	 * 
	 * @param param
	 * @return
	 * @throws SQLException
	 */
	protected PoolConnection newPoolConnection(ResourceConfig param, boolean isUsed) throws SQLException {
		Connection conn;
		try {
			this.getClass().getClassLoader().loadClass(param.getDriver()).newInstance();
			conn = DriverManager.getConnection(param.getUrl(), param.getUsername(), param.getPassword());
			if (isUsed&&StringUtils.notEmpty(param.getValidationQuery())) {
				SqlExecutor run = new SQLRunner(new BasicTransaction(conn));
				run.execute(param.getValidationQuery());
			}
		} catch (Exception e) {
			throw new SQLException("连接数据库失败：" + param,e);
		}
		// 代理将要返回的连接对象
		return new PoolConnection(connectionId.decrementAndGet(), conn, isUsed);
	}

	/**
	 * 获取数据库连接
	 */
	public Connection getConnection(String user, String password) throws SQLException {
		// 首先从连接池中找出空闲的对象
		Connection conn = getFreeConnection(0);
		if (conn == null) {
			// 没有超过连接数，重新获取一个数据库的连接
			if (user != null && password != null) {
				param.setUsername(user);
				param.setPassword(password);
				return getFreeConnection(0);
			}
		}
		return conn;
	}

	/**
	 * 从连接池中取一个空闲的连接
	 * 
	 * @param time 如果该参数值为 0 则没有连接时只是返回一个 null 否则的话等待 nTimeout
	 *        毫秒看是否还有空闲连接，如果没有抛出异常
	 * @return Connection
	 * @throws SQLException
	 */
	protected Connection getFreeConnection(long time) throws SQLException {
		// 查询空闲连接
		try {
			PoolConnection _conn=connectionPool.borrowObject(time);
			return _conn.getConnection();
		} catch (Exception e) {
			throw new SQLException("获取数据库连接失败",e);
		}
		
	}

	

	/**
	 * Connection 的一个代理类 只是代理了 Connection的close()方法
	 * 
	 * @author strive
	 *
	 */
	class PoolConnection implements InvocationHandler {
		/**
		 * 要代理的方法名
		 */
		private final static String CLOSE_METHOD_NAME = "close";

		private final static String TOSTRING_METHOD_NAME = "toString";
		/** 真正的连接 */
		protected Connection conn = null;
		// 数据库的忙状态
		private volatile boolean isUsed = false;
		/** 代理的连接 */
		private Connection proxyConn;
		/** 连接id */
		private long id;

		PoolConnection(long id, Connection conn, boolean isUsed) {
			this.conn = conn;
			this.isUsed = isUsed;
			this.id = id;
		}
		/**
		 * Returns the conn.
		 * 
		 * @return Connection
		 */
		public Connection getConnection() {
			if (proxyConn == null) {
				// 返回数据库连接 conn 的接管类，以便截住 close 方法
				Class[] interfaces = new Class[] {Connection.class};
				proxyConn = (Connection) Proxy.newProxyInstance(conn.getClass().getClassLoader(), interfaces, this);
			}
			return proxyConn;
		}

		/**
		 * 该方法真正的关闭了数据库的连接
		 * 
		 * @throws SQLException
		 */
		synchronized void close(){
			try{
				// 由于类属性 conn 是没有被接管的连接，因此一旦调用 close 方法后就直接关闭连接
				this.isUsed = true;
				conn.close();
				connectionPool.invalidateObject(this);
			}catch(Exception e){
				logger.error("闭关连接失败"+this,e);
			}
		}

		/**
		 * Returns the inUse.
		 * 
		 * @return boolean
		 */
		public synchronized boolean isUsed() {
			return isUsed;
		}

		/**
		 * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object,
		 *      java.lang.reflect.Method, java.lang.Object)
		 */
		public Object invoke(Object proxy, Method m, Object[] args) throws Throwable {
			synchronized (conn) {
				if (TOSTRING_METHOD_NAME.equals(m.getName())){
					//代理重写了toString方法
					return this.toString();
				}else if (CLOSE_METHOD_NAME.equals(m.getName())) {
					// 判断是否调用了 close 的方法，如果调用 close 方法则把连接回归池中
					connectionPool.returnObject(this);
					return null;
				}else if(isUsed){
					return m.invoke(conn, args);
				}else{
					throw new SQLException("Connection "+conn+" is not active ");
				}
			}
		}

		/**
		 * Sets the inUse.
		 * 
		 * @param inUse The inUse to set
		 */
		public synchronized void setIsUsed(boolean isUsed) {
			this.isUsed = isUsed;
		}

		public String toString() {
			return super.toString() + "-proxy" + id + "--" + conn.toString();
		}
		
	}
	
	class ConnPooledObjectFactory  implements PooledObjectFactory<PoolConnection>{

		@Override
		public void activateObject(PooledObject<PoolConnection> obj) throws Exception {
			PoolConnection conn=obj.getObject();
			conn.setIsUsed(true);
		}
		@Override
		public void destroyObject(PooledObject<PoolConnection> obj) throws Exception {
			obj.getObject().close();
		}

		@Override
		public PooledObject<PoolConnection> makeObject() throws Exception {
			return new DefaultPooledObject<PoolConnection>(newPoolConnection(param, false));
		}

		@Override
		public void passivateObject(PooledObject<PoolConnection> obj) throws Exception {
			PoolConnection _conn=obj.getObject();
			_conn.setIsUsed(false);
			_conn.conn.setAutoCommit(true);
		}

		@Override
		public boolean validateObject(PooledObject<PoolConnection> obj) {
			try {
				return !obj.getObject().conn.isClosed();
			} catch (SQLException e) {
				logger.error("检验连接异常", e);
				return false;
			}
		}
	}

	@Override
	public void close() {
		this.connectionPool.close();
	}

	@Override
	public int getConnectedCount() {
		return connectionPool.getNumActive();
	}

}
