package org.smile.transaction;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;

import javax.sql.DataSource;

import org.smile.collection.CollectionUtils;
import org.smile.db.DbUtils;
import org.smile.db.Transaction;

/***
 * 有事务管理的事务
 * 使用动态代理
 * @author 胡真山
 */
public class SmileTransactoin implements Transaction,InvocationHandler{
	/**当前的连接*/
	protected Connection connection;
	/**当前的数据源*/
	private DataSource dataSource;
	/**
	 * 代理时不执行的方法 
	 * 统一的事务管理的类中执行
	 */
	private Set<String> notExeMethod=CollectionUtils.hashSet("close","commit","setAutoCommit");
	/**
	 * 用于保存初始时是否是自动提交
	 */
	private boolean autoCommit;
	/**代理后的连接*/
	private Connection proxyConnection;

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		if(!notExeMethod.contains(method.getName())){
			return method.invoke(connection, args);
		}
		return null;
	}
	
	public SmileTransactoin(DataSource dataSource) throws SQLException{
		this.dataSource=dataSource;
		this.connection=dataSource.getConnection();
	}
	
	@Override
	public Connection getConnection() throws SQLException {
		if(proxyConnection==null){
	    	Class[] interfaces=new Class[]{Connection.class};
	        Object proxyConn =Proxy.newProxyInstance(connection.getClass().getClassLoader(),interfaces,this); 
	        proxyConnection=(Connection)proxyConn;
		}
        return proxyConnection; 
	}

	@Override
	public void begin() throws SQLException {
		this.autoCommit=connection.getAutoCommit();
		this.connection.setAutoCommit(false);
	}

	@Override
	public void commit() throws SQLException {
		
	}
	
	protected void commitRealy() throws SQLException{
		this.connection.commit();
		this.connection.setAutoCommit(autoCommit);
	}

	@Override
	public void close() throws SQLException {
		if(TransactionUtils.isSynTransaction()){

		}else{
			this.connection.setAutoCommit(autoCommit);
			DbUtils.close(this.connection);
		}
	}
	
	public DataSource getDataSource(){
		return dataSource;
	}

	@Override
	public void rollback() throws SQLException {
		this.connection.rollback();
	}
}
