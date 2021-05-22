package org.smile.orm.dao;

import java.sql.SQLException;
import java.util.Collection;

import javax.sql.DataSource;

import org.smile.db.DbConstans;
import org.smile.db.Dialect;
import org.smile.db.Transaction;
import org.smile.db.TransactionHandler;
import org.smile.db.sql.BoundSql;
import org.smile.db.sql.SQLRunner;
import org.smile.log.LoggerHandler;
import org.smile.math.MathUtils;
import org.smile.orm.OrmApplication;
import org.smile.orm.executor.MappedOperator;
import org.smile.orm.result.ResultType;

/**
 * XML dao 对数据库操作的基础类
 * @author 胡真山
 */
public class BaseExecutor implements Executor, LoggerHandler {
	/**执行的数据源*/
	protected DataSource dataSource;

	private MethodApater apater;

	private Dialect dialect = DbConstans.DIALECT;
	/**事务处理器*/
	private TransactionHandler transactionHandler = new TransactionHandler();
	
	private ExecutorBoundHandler boundHandler;

	public BaseExecutor(OrmApplication application) {
		apater = new MethodApater(application);
		ExecutorBoundHandler handler=new BaseExecutorBoundHandler();
		handler.setExecutor(this);
		//此代码是为了可以插件代理
		boundHandler=application.plugin(handler);
	}
	
	/**获取事务*/
	public Transaction getTransaction() throws SQLException {
		return transactionHandler.getTransaction(dataSource);
	}

	@Override
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public Object execute(MappedOperator operator, Object param) throws SQLException {
		return this.apater.handler(operator, param);
	}

	@Override
	public Object queryPage(MappedOperator operator, Object param, int page, int size) throws SQLException {
		SQLRunner runner = operator.createSQLRunner(getTransaction(), operator, param);
		try {
			BoundSql boudSql = boundHandler.buildBoundSql(operator, param);
			runner.setDbDialect(dialect);
			return runner.queryPageSql(boudSql, page, size);
		} finally {
			endTransaction(runner.getTransaction());
		}
	}

	@Override
	public Object update(MappedOperator operator, Object param) throws SQLException {
		return executeMethod(operator, param);
	}

	@Override
	public Object delete(MappedOperator operator, Object param) throws SQLException {
		return executeMethod(operator, param);
	}

	@Override
	public Object query(MappedOperator operator, Object param) throws SQLException {
		return executeMethod(operator, param);
	}
	/**
	 * 执行一个数据库操作
	 * @param operator 操作
	 * @param param 操作参数
	 * @return
	 * @throws SQLException
	 */
	protected Object executeMethod(MappedOperator operator, Object param) throws SQLException {
		SQLRunner runner = operator.createSQLRunner(getTransaction(), operator, param);
		try {
			BoundSql boudSql = boundHandler.buildBoundSql(operator, param);
			ResultType type = operator.getResultType();
			return operator.getOperator().execute(runner, type, boudSql);
		} finally {
			endTransaction(runner.getTransaction());
		}
	}
	/**
	 * 操作结束时事务处理
	 * @param transaction
	 * @throws SQLException
	 */
	protected void endTransaction(Transaction transaction) throws SQLException {
		transaction.close();
	}

	@Override
	public Object insert(MappedOperator operator, Object param) throws SQLException {
		return executeMethod(operator, param);
	}

	public void setDialect(Dialect dialect) {
		this.dialect = dialect;
	}

	@Override
	public DataSource getDataSource() {
		return dataSource;
	}

	public void setTransactionHandler(TransactionHandler transactionHandler) {
		this.transactionHandler = transactionHandler;
	}

	@Override
	public Object batch(MappedOperator operator, BoundSql boundSql, Collection batchList) throws SQLException {
		SQLRunner runner = operator.createSQLRunner(getTransaction(), operator, batchList);
		try {
			ResultType type = operator.getResultType();
			int[] rows = runner.batch(boundSql, batchList);
			int result = MathUtils.add(rows);
			if (type.getType() == boolean.class||type.getType()==Boolean.class) {
				return result > 0;
			} else {
				return result;
			}
		} finally {
			endTransaction(runner.getTransaction());
		}
	}

	@Override
	public Dialect getDialect() {
		return dialect;
	}

	@Override
	public Object queryLimit(MappedOperator operator, Object param, int offset, int limit) throws SQLException {
		SQLRunner runner = operator.createSQLRunner(getTransaction(), operator, param);
		try {
			BoundSql boudSql = boundHandler.buildBoundSql(operator, param);
			runner.setDbDialect(dialect);
			return runner.queryLimitSql(boudSql, offset, limit);
		} finally {
			endTransaction(runner.getTransaction());
		}
	}
}
