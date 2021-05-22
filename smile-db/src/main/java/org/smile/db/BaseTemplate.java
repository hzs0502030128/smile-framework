package org.smile.db;

import java.sql.SQLException;

import org.smile.db.sql.DaoTarget;

public interface BaseTemplate extends DaoTarget{
	/**
	 * 初始化事务控制
	 * @return
	 * @throws SQLException
	 */
	public Transaction initTransaction();
	/**
	 * 操作结束时对事务的处理
	 * @param transaction
	 * @throws SQLException
	 */
	public void endTransaction(Transaction transaction);
	
	
}
