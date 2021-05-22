package org.smile.db;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * 数据源的一个实例 
 * 但并不是对数据源的实现，只是拿到一个数据源的引用 
 * 来服务
 * @author strive
 *
 */
public class DataSourceBean implements DataSource {
	/**
	 * 数据源实例引用
	 */
	private DataSource ds;
	/**
	 * 数据源的名称
	 */
	private String name;
	
	/**
	 * 空的构造方法，只类不进行数据源的实现 ，
	 * 只是拿到一个数据源的引用来提供服务
	 */
	public DataSourceBean(){}
	
	/**
	 * 设置数据源的名称，并实例化
	 * @param name
	 * @throws NamingException
	 */
	public void setName(String name) throws NamingException{
		this.name=name;
		try{
			this.ds=DbManager.getDataSource(name);
		}catch(Throwable e){
			throw new NamingException("不存在的数据源名称："+name);
		}
		if(ds==null){
			throw new NamingException("can find a datasource named "+name);
		}
	}
	
	/**
	 * 查看数据源的名称
	 * @return
	 */
	public String getName(){
		return name;
	}
	public Connection getConnection() throws SQLException {
		Connection conn= ds.getConnection();
		return conn;
	}

	public Connection getConnection(String username, String password)
			throws SQLException {
		return ds.getConnection(username, password);
	}

	public PrintWriter getLogWriter() throws SQLException {
		return ds.getLogWriter();
	}

	public int getLoginTimeout() throws SQLException {
		return ds.getLoginTimeout();
	}

	public void setLogWriter(PrintWriter out) throws SQLException {
		ds.setLogWriter(out);
	}

	public void setLoginTimeout(int seconds) throws SQLException {
		ds.setLoginTimeout(seconds);
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return ds.isWrapperFor(iface);
	}

	public <T> T unwrap(Class<T> iface) throws SQLException {
		return ds.unwrap(iface);
	}

	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		return ds.getParentLogger();
	}
}
