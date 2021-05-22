package org.smile.orm.base;

import java.sql.SQLException;
import java.util.List;

import org.smile.db.PageModel;
import org.smile.db.Transaction;
import org.smile.db.TransactionHandler;
import org.smile.db.sql.SQLRunner;
import org.smile.orm.base.impl.BaseDAOImpl;
import org.smile.orm.base.impl.OrmObjRowHandler;
import org.smile.orm.record.OrmRecordConfig;

public class OrmDataTemplate extends BaseDAOImpl {
	
	public OrmDataTemplate(){}
	
	public OrmDataTemplate(TransactionHandler transactionHandler){
		this.transactionHandler=transactionHandler;
	}

	/***
	 * 执行一个sql语句返回
	 * @param sql
	 * @param resultClass
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public <E> List<E> querySqlForOrmObj(String sql,Class resultClass,Object...params) throws SQLException{
		Transaction transaction = initTransaction();
		try {
			SQLRunner runner = new SQLRunner(transaction, new OrmObjRowHandler(resultClass));
			return runner.query(sql,params);
		} finally {
			endTransaction(transaction);
		}
	}
	/***
	 * 执行分页查询语句
	 * @param sql
	 * @param resultClass
	 * @param page
	 * @param size
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public <E> PageModel<E> queryPageSqlForOrmObj(String sql,Class resultClass,int page,int size,Object...params) throws SQLException{
		Transaction transaction = initTransaction();
		try {
			SQLRunner runner = new SQLRunner(transaction, new OrmObjRowHandler(resultClass));
			return runner.queryPageSql(sql, page, size,params);
		} finally {
			endTransaction(transaction);
		}
	}

	/**
	 * 设置事务处理类
	 * @param transactionHandler
	 */
	public void setTransactionHandler(String transactionHandler) {
		try {
			Class clazz=Class.forName(transactionHandler);
			this.transactionHandler=(TransactionHandler)clazz.newInstance();
		} catch (Exception e) {
			throw new RuntimeException("实始化 transactionHandler"+transactionHandler+"失败",e);
		}
	}
	/**
	 * 把当前的操作模板设置到默认配置中
	 */
	public void init2OrmRecordConfig(){
		OrmRecordConfig.getInstance().setOrmDaoSupport(this);
	}
	
}
