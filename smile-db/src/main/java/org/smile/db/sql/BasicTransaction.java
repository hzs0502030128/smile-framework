package org.smile.db.sql;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.smile.db.Transaction;
/**
 * 最基本的一个事务操作实现  
 * 使用手动提交
 * @author 胡真山
 *
 */
public class BasicTransaction implements Transaction {
	/**当前的数据库连接*/
	protected Connection conn;
	/**是否自动提交，用于暂存连接原来是否自动提交*/
	protected boolean autoCommit;
	
	public BasicTransaction(Connection connection) throws SQLException{
		this.conn=connection;
		this.autoCommit=connection.getAutoCommit();
	}
	
	protected BasicTransaction(){}
	
	public  BasicTransaction(DataSource ds) throws SQLException {
		this(ds.getConnection());
	}
	
	@Override
	public Connection getConnection() throws SQLException {
		return conn;
	}

	@Override
	public void begin() throws SQLException {
		//开启手动提交方法
		conn.setAutoCommit(false);
	}

	@Override
	public void commit() throws SQLException {
		conn.commit();
	}

	@Override
	public void close() throws SQLException {
		//恢复是否自动提交
		conn.setAutoCommit(autoCommit);
		//关闭连接
		conn.close();
	}

	@Override
	public void rollback() throws SQLException {
		conn.rollback();
	}

}
