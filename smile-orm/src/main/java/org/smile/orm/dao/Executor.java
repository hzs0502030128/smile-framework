package org.smile.orm.dao;

import java.sql.SQLException;
import java.util.Collection;

import javax.sql.DataSource;

import org.smile.db.Dialect;
import org.smile.db.Transaction;
import org.smile.db.TransactionHandler;
import org.smile.db.sql.BoundSql;
import org.smile.orm.executor.MappedOperator;
/**
 * 数据库操作执行接口
 * 可以通过配置拦截器拦截
 * @author 胡真山
 * 2015年10月30日
 */
public interface Executor {
	/**
	 * 设置执行的方言
	 * @param dialect
	 */
	public void setDialect(Dialect dialect);
	/**
	 * 设置数据库的数据源
	 * @param dataSource
	 */
	public void setDataSource(DataSource dataSource);
	/**
	 * 设置事务处理者 
	 * @param transactionHandler
	 */
	public void setTransactionHandler(TransactionHandler transactionHandler);
	/**
	 * 获得会话
	 * @return
	 * @throws SQLException
	 */
	public Transaction getTransaction() throws SQLException;
	/**
	 * 获取数据源
	 * @return
	 */
	public DataSource getDataSource();
	/**
	 * 执行一个数据库操作  此方法 为 update insert query batch delete等方法的入口
	 * @param operator 一个操作方法
	 * @param param 操作方法的参数
	 * @return
	 * @throws SQLException
	 */
	public Object execute(MappedOperator operator,Object param) throws SQLException;
	/**
	 * 对数据库执行一个更新操作方法
	 * @param operator
	 * @param param 操作方法的参数
	 * @return
	 * @throws SQLException
	 */
	public Object update(MappedOperator operator,Object param) throws SQLException;
	/**
	 * 删除操作
	 * @param operator
	 * @param param
	 * @return
	 * @throws SQLException
	 */
	public Object delete(MappedOperator operator,Object param) throws SQLException;
	/**
	 * 查询操作
	 * @param operator
	 * @param param
	 * @return
	 * @throws SQLException
	 */
	public Object query(MappedOperator operator,Object param) throws SQLException;
	/**
	 * 插入操作
	 * @param operator
	 * @param param
	 * @return
	 * @throws SQLException
	 */
	public Object insert(MappedOperator operator,Object param) throws SQLException;
	/**
	 * 分页查询操作
	 * @param operator 
	 * @param param 操作参数的方法
	 * @param page 当前页
	 * @param size 每页条数
	 * @return
	 * @throws SQLException
	 */
	public Object queryPage(MappedOperator operator,Object param,int page,int size) throws SQLException;
	/**
	 * limit查询
	 * @param operator
	 * @param param
	 * @param offset 偏移行数
	 * @param limit 限制条数
	 * @return
	 * @throws SQLException
	 */
	public Object queryLimit(MappedOperator operator,Object param,int offset,int limit) throws SQLException;
	/**
	 * 批量操作
	 * @param operator
	 * @param boundSql
	 * @param batchList
	 * @return 
	 * @throws SQLException
	 */
	public Object batch(MappedOperator operator, BoundSql boundSql, Collection batchList) throws SQLException;
	/***
	 * 获取方言
	 * @return
	 */
	public Dialect getDialect();
}
