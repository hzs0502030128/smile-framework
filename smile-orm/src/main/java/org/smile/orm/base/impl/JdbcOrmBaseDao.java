package org.smile.orm.base.impl;

import org.smile.db.Transaction;
/**
 * 手动控制事务使用
 * @author 胡真山
 */
public class JdbcOrmBaseDao extends BaseDAOImpl{
	
	protected Transaction transaction;
	
	public JdbcOrmBaseDao(Transaction transaction){
		this.transaction=transaction;
	}
	
	@Override
	public void endTransaction(Transaction transaction){
		
	}

	@Override
	public Transaction initTransaction(){
		return transaction;
	}
	
}
