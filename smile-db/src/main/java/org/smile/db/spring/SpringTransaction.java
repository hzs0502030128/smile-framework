package org.smile.db.spring;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.smile.db.sql.BasicTransaction;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class SpringTransaction  extends BasicTransaction {
	/**
	 * 事务管理的数据源
	 */
	private DataSource dataSource;
	/**
	 * 是否已经Spring开启了事务
	 */
	private boolean isSynchronizationActive=false;
	
	public SpringTransaction(DataSource dataSource) throws SQLException{
		this.dataSource=dataSource;
		conn= DataSourceUtils.getConnection(dataSource);
		this.autoCommit=conn.getAutoCommit();
		isSynchronizationActive=TransactionSynchronizationManager.isSynchronizationActive();
	}

	@Override
	public void begin() throws SQLException {
		if(!isSynchronizationActive){
			super.begin();
		}
	}

	@Override
	public void commit() throws SQLException {
		if(!isSynchronizationActive){
			conn.commit();
		}
	}

	@Override
	public void close() throws SQLException {
		if(!isSynchronizationActive){
			conn.setAutoCommit(autoCommit);
		}
		DataSourceUtils.doReleaseConnection(conn, dataSource);
	}

	@Override
	public void rollback() throws SQLException {
		if(!isSynchronizationActive){
			super.rollback();
		}
	}
}
