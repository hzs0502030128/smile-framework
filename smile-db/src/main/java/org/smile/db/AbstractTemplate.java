package org.smile.db;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.smile.collection.CollectionUtils.GroupKey;
import org.smile.db.handler.RowHandler;
import org.smile.db.sql.BoundSql;
import org.smile.db.sql.NamedBoundSql;
import org.smile.db.sql.SQLRunner;

public abstract class AbstractTemplate implements BaseTemplate{
	/**
	 * 操作的目标数据源
	 */
	protected DataSource dataSource;
	
	/**
	 * 数据库方言类型
	 */
	protected Dialect dialect = DbConstans.DIALECT;
	
	/**用于处理事务的提交*/
	protected TransactionHandler transactionHandler;
	
	/**
	 * 创建事务管理
	 * @return
	 * @throws SQLException
	 */
	@Override
	public Transaction initTransaction(){
		try {
			return transactionHandler.getTransaction(dataSource);
		} catch (SQLException e) {
			throw new SqlRunException(e);
		}
	}
	/**
	 * 设置数据库方言
	 * @param dialect
	 */
	public void setDialect(Dialect dialect) {
		this.dialect = dialect;
	}
	
	@Override
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public DataSource getDataSource() {
		return this.dataSource;
	}
	
	@Override
	public <E> E queryFirst(BoundSql boundSql, RowHandler rowHandler){
		Transaction transaction=initTransaction();
		try{
			SQLRunner runner = new SQLRunner(transaction,rowHandler);
			runner.setDbDialect(dialect);
			return runner.queryFirst(boundSql);
		}finally{
			endTransaction(transaction);
		}
	}
	
	/**
	 * @param boundSql 查询参数封装
	 * @param rowHandler 数据行转换器
	 * @return
	 * @throws SQLException
	 */
	@Override
	public <E> List<E> query(BoundSql boundSql,RowHandler rowHandler){
		Transaction transaction=initTransaction();
		try{
			SQLRunner runner = new SQLRunner(transaction,rowHandler);
			runner.setDbDialect(dialect);
			return runner.query(boundSql);
		}finally{
			endTransaction(transaction);
		}
	}
	
	@Override
	public <E> List<E> queryLimit(BoundSql boundSql,RowHandler rowHandler,long offset,int limit){
		Transaction transaction=initTransaction();
		try{
			SQLRunner runner = new SQLRunner(transaction,rowHandler);
			runner.setDbDialect(dialect);
			return runner.queryLimitSql(boundSql, offset, limit);
		}finally{
			endTransaction(transaction);
		}
	}
	
	/**
	 * 
	 * @param boundSql 查询语句封装
	 * @param rowHandler 数据行转换器
	 * @param groupKey 结果集分组
	 * @return
	 * @throws SQLException
	 */
	@Override
	public <K,E> Map<K,E> queryForKeyMap(BoundSql boundSql,RowHandler rowHandler,GroupKey<K, E> groupKey){
		Transaction transaction=initTransaction();
		try{
			SQLRunner runner = new SQLRunner(transaction,rowHandler);
			return runner.queryForKeyMap(boundSql, groupKey);
		}finally{
			endTransaction(transaction);
		}
	}
	
	/**
	 * 返回分组Map
	 * @param boundSql
	 * @param rowHandler
	 * @param groupKey
	 * @return
	 * @throws SQLException
	 */
	@Override
	public <K,E> Map<K,List<E>> queryForGroupMap(BoundSql boundSql,RowHandler rowHandler,GroupKey<K, E> groupKey){
		Transaction transaction=initTransaction();
		try{
			SQLRunner runner = new SQLRunner(transaction,rowHandler);
			return runner.queryForGroupMap(boundSql, groupKey);
		}finally{
			endTransaction(transaction);
		}
	}
	
	/**
	 *  查询前多少条数据
	 * @param boundSql 查询参数封装
	 * @param rowHandler 数据行转换器
	 * @return
	 * @throws SQLException
	 */
	@Override
	public <E> List<E> queryTop(int top,BoundSql boundSql,RowHandler rowHandler){
		Transaction transaction=initTransaction();
		try{
			SQLRunner runner = new SQLRunner(transaction,rowHandler);
			runner.setDbDialect(dialect);
			return runner.queryTop(boundSql,top);
		}finally{
			endTransaction(transaction);
		}
	}
	
	/**
	 * 分页查询对象
	 * 
	 * @param c 返回对象的封装类型
	 * @param whereSql 过滤条件
	 * @return
	 * @throws Exception
	 */
	@Override
	public <E> PageModel<E> queryPageSql(RowHandler rowHandler,BoundSql boundSql, int page, int size){
		Transaction transaction=initTransaction();
		try{
			SQLRunner sqlRunner = new SQLRunner(transaction, rowHandler);
			sqlRunner.setDbDialect(dialect);
			PageModel<E> pageModel = sqlRunner.queryPageSql(boundSql, page, size);
			return pageModel;
		}finally{
			endTransaction(transaction);
		}
	}
	
	/**
	 * 查询一个 :name 方式占位的sql语句
	 * @param sql
	 * @param rowHandler
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public <E> List<E> queryNamedSql(String sql,RowHandler rowHandler,Object params){
		BoundSql boundSql=new NamedBoundSql(sql, params);
		return query(boundSql, rowHandler);
	}
	
	@Override
	public void endTransaction(Transaction transaction) {
		DbUtils.close(transaction);
	}

}
