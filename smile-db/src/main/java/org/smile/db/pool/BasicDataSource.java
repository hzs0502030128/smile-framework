package org.smile.db.pool;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.smile.db.config.ResourceConfig;
import org.smile.db.sql.BasicTransaction;
import org.smile.db.sql.SQLRunner;
import org.smile.db.sql.SqlExecutor;
import org.smile.util.StringUtils;

/**
 * 一个基本的数据源实现类
 * 
 * @author strive
 */
public class BasicDataSource extends AbstractDataSource{
	/**
	 * 标记记线程是否要运行
	 */
	private boolean isRun = true;

	/**
	 * 数据库连接清理线程
	 */
	private ConnectionClear clearor;
	/**
	 * 空闲中的连接
	 */
	Queue<PoolConnection> freeConns=new ConcurrentLinkedQueue<PoolConnection>();
	/**
	 * 使用中的数据库连接
	 */
	Queue<PoolConnection> useredConns = new ConcurrentLinkedQueue<PoolConnection>();
	/** 清理空闲连接的时间 */
	protected static final int CLEAR_TIMES = 10 * 60 * 1000;
	/**
	 * 构造一个数据数据源 在不使用此数据源，即把此数据源从连接池中删除必须关闭此数据源
	 * 
	 * @param param
	 * @throws SQLException
	 */
	public BasicDataSource(ResourceConfig param) throws SQLException {
		this.param = param;
		//初始化一个连接
		freeConns.add(newPoolConnection(param, false));
		//启用清理进程
		clearor = new ConnectionClear();
		clearor.start();
	}

	/**
	 * 得到连接数
	 * 
	 * @return
	 */
	@Override
	public synchronized int getAllCount() {
		return this.freeConns.size() + this.useredConns.size();
	}

	/**
	 * 得到空闲连接个数
	 * 
	 * @return
	 */
	@Override
	public synchronized int getFreeCount() {
		return this.freeConns.size();
	}

	/**
	 * 创建一个新的连接
	 * 
	 * @param param
	 * @return
	 * @throws SQLException
	 */
	private PoolConnection newPoolConnection(ResourceConfig param, boolean isUsed) throws SQLException {
		Connection conn;
		try {
			this.getClass().getClassLoader().loadClass(param.getDriver()).newInstance();
			conn = DriverManager.getConnection(param.getUrl(), param.getUsername(), param.getPassword());
			if (isUsed&&StringUtils.notEmpty(param.getValidationQuery())) {
				SqlExecutor run = new SQLRunner(new BasicTransaction(conn));
				run.execute(param.getValidationQuery());
			}
		} catch (Exception e) {
			throw new SQLException("连接数据库失败：" + param ,e);
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
			// 判断是否超过最大连接数 , 如果超过最大连接数
			// 则等待一定时间查看是否有空闲连接 , 否则抛出异常告诉用户无可用连接
			if (getAllCount() >= param.getMaxActive()) {
				// 达到最大时候，等待空闲连接
				conn = getFreeConnection(param.getMaxWaitTime());
			} else {
				// 没有超过连接数，重新获取一个数据库的连接
				if (user != null && password != null) {
					param.setUsername(user);
					param.setPassword(password);
				}
				// 代理将要返回的连接对象
				PoolConnection _conn = newPoolConnection(param, true);
				synchronized (freeConns) {
					useredConns.add(_conn);
				}
				conn = _conn.getConnection();
			}
		}
		// 最后一次连接时间设置为当前时间
		this.lastConnectTime = System.currentTimeMillis();
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
		// 设置尝试连接的间隔
		final int TRY_TIME = 1000;
		Connection conn = null;
		// 查询空闲连接
		synchronized (freeConns) {
			while (freeConns.size() > 0) {
				PoolConnection _conn = (PoolConnection) freeConns.poll();
				useredConns.add(_conn);
				if (!_conn.isUsed()) {
					conn = _conn.getConnection();
					_conn.setIsUsed(true);
					break;
				}
			}
		}
		// 没有空闲时，等待一个间隔就尝试一次，尝试的总时间为time
		while (conn == null && time > 0) {
			// 等待 nTimeout 毫秒以便看是否有空闲连接
			try {
				Thread.sleep(TRY_TIME);
			} catch (Exception e) {}
			conn = getFreeConnection(0);
			// 取一次 ，取的次数中等待的时间长决定 取的次数为：等待时间/5000
			time -= TRY_TIME;
		}
		if (conn == null) {
			if (this.getAllCount() >= param.getMaxActive()) {
				throw new SQLException("没有可用的数据库连接:wait time out ……………………");
			}
		}
		return conn;
	}
	/**
	 * 获得数据库连接
	 */
	public Connection getConnection() throws SQLException {
		Connection conn = this.getConnection(null, null);
		if (logger.isDebugEnabled()) {
			logger.debug("free count:" + getFreeCount());
			logger.debug("used count:" + getConnectedCount());
		}
		return conn;
	}

	/**
	 * 关闭数据源 会关闭源中的所有连接
	 */
	public synchronized void close() {
		for (PoolConnection conn : freeConns) {
			conn.close();
		}
		for (PoolConnection conn : useredConns) {
				conn.close();
		}
		this.isRun = false;
		// 唤醒清理线程 结束线程池程序
		clearor.interrupt();
	}

	/**
	 * 使用了的连接数
	 * 
	 * @return
	 */
	@Override
	public int getConnectedCount() {
		return useredConns.size();
	}

	/**
	 * 连接清理线程
	 * 
	 * @author strive
	 *
	 */
	private class ConnectionClear extends Thread {
		@Override
		public void run() {
			while (isRun) {
				try {
					// 清理一次
					this.sleep(CLEAR_TIMES);
					clearTimeout();
				} catch (Exception e) {
					logger.warn("连接清理线程sleep中被中断,可能是调用了数据源关闭方法了,如不是则可能出现了未知的异常:[url:" + param.getUrl() + ",user:" + param.getUsername() + "]");
				}
			}
			logger.info("连接池[url:" + param.getUrl() + ",user:" + param.getUsername() + "]清理线程被停止,可能是连接池已关闭");
		}

		private void clearTimeout() {
			long currentTime = System.currentTimeMillis();
			if (freeConns.size() > 0) {
				PoolConnection con = freeConns.poll();
				while (con != null) {
					// 如果超时关闭
					if (currentTime - con.getLastAccessTime() > param.getTimeOutValue()) {
						synchronized (freeConns) {
							// 保留设置的最少连接数
							if (getAllCount() > param.getMinActive()) {
								try {
									con.close();
									logger.info("清理空闲连接" + con + "成功");
								} catch (Exception e) {
									logger.error("清理空闲的连接出现错误", e);
								}
							}
						}
						if (freeConns.size() > 0) {
							con = freeConns.poll();
						} else {
							con = null;
						}
					} else {
						con = null;
					}
				}
			}
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
		private Connection conn = null;
		// 数据库的忙状态
		private volatile boolean isUsed = false;
		/** 用户最后一次访问该连接方法的时间 */
		private volatile long lastAccessTime = System.currentTimeMillis();
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
				Class[] interfaces = new Class[] { Connection.class };
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
			}catch(SQLException e){
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
			Object obj = null;
			// 判断是否调用了 close 的方法，如果调用 close 方法则把连接置为无用状态
			if (CLOSE_METHOD_NAME.equals(m.getName())) {
				if (isUsed()) {
					synchronized (freeConns) {
						setIsUsed(false);
						conn.setAutoCommit(true);
						useredConns.remove(this);
						freeConns.add(this);
						if(logger.isDebugEnabled()){
							logger.debug("free count:" + getFreeCount());
							logger.debug("used count:" + getConnectedCount());
						}
					}
				}
			} else if (TOSTRING_METHOD_NAME.equals(m.getName())) {
				obj = this.toString();
			} else {
				obj = m.invoke(conn, args);
			}
			// 设置最后一次访问时间，以便及时清除超时的连接
			lastAccessTime = System.currentTimeMillis();
			return obj;
		}

		/**
		 * Returns the lastAccessTime.
		 * 
		 * @return long
		 */
		public synchronized long getLastAccessTime() {
			return lastAccessTime;
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

}
