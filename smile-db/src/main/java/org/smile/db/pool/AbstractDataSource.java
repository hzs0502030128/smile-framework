package org.smile.db.pool;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.concurrent.atomic.AtomicLong;

import javax.sql.DataSource;

import org.smile.db.config.ResourceConfig;
import org.smile.log.LoggerHandler;
import org.smile.plugin.Interceptor;
import org.smile.plugin.Plugin;

public abstract class AbstractDataSource implements DataSource, LoggerHandler{
	/**
	 * 最后一次连接数据源的时间
	 */
	protected volatile long lastConnectTime;
	/**
	 * 数据源参数
	 */
	protected ResourceConfig param;
	
	protected AtomicLong connectionId = new AtomicLong(0);
	
	public PrintWriter getLogWriter() throws SQLException {
		throw new SQLException("此方法还没有实现");
	}

	public int getLoginTimeout() throws SQLException {
		return (int) (System.currentTimeMillis() - this.lastConnectTime);
	}

	public void setLogWriter(PrintWriter printwriter) throws SQLException {
		throw new SQLException("此方法还没有实现");
	}

	public void setLoginTimeout(int i) throws SQLException {
		this.lastConnectTime = System.currentTimeMillis() - i;
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
	 * 空闲连接数
	 * @return
	 */
	public abstract int getFreeCount();

	/**
	 * 关闭数据源 会关闭源中的所有连接
	 */
	public abstract void close();

	/**
	 * 使用了的连接数
	 * 
	 * @return
	 */
	public abstract int getConnectedCount();
	/**
	 * 所有的连接数
	 * @return
	 */
	public abstract int getAllCount();
	

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return Interceptor.class.isAssignableFrom(iface);
	}

	public <T> T unwrap(Class<T> iface) throws SQLException {
		Interceptor interceptor=null;
		try {
			interceptor = (Interceptor)iface.newInstance();
		}catch (Exception e) {
			throw new SQLException(e);
		}
		return (T)Plugin.wrap(this, interceptor);
	}

	public java.util.logging.Logger getParentLogger() throws SQLFeatureNotSupportedException {
		throw new SQLFeatureNotSupportedException("not support method ");
	}
}
