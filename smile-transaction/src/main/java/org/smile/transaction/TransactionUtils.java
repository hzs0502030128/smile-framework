package org.smile.transaction;

import java.sql.SQLException;
import java.util.Map;

import javax.sql.DataSource;

import org.smile.collection.ThreadLocalMap;
import org.smile.db.Transaction;
import org.smile.log.LoggerHandler;

public class TransactionUtils implements LoggerHandler {

	private static Map<DataSource, Transaction> transactions = new ThreadLocalMap<DataSource, Transaction>();
	
	private static TransactionState statusStack = new TransactionState();
	
	/**
	 * 
	 * @param dataSource
	 * @return
	 * @throws SQLException
	 */
	public static Transaction getTransaction(DataSource dataSource) throws SQLException {
		Boolean needBegin = statusStack.peek();
		if (needBegin == null) {
			return new SmileTransactoin(dataSource);
		} else {
			Transaction transaction = transactions.get(dataSource);
			if (transaction == null) {
				transaction = new SmileTransactoin(dataSource);
				if (needBegin) {//是否需要开启事务
					transaction.begin();
				}
				transactions.put(dataSource, transaction);
			}
			return transaction;
		}
	}
	

	public static void closeTransaction(DataSource dataSource) throws SQLException {
		Transaction transaction = transactions.get(dataSource);
		if (transaction != null) {
			if (transaction instanceof SmileTransactoin) {
				((SmileTransactoin) transaction).connection.close();
			} else {
				transaction.close();
			}
		}
	}

	protected static void commitAllTransactions() throws SQLException {
		for (Transaction t : transactions.values()) {
			if (t instanceof SmileTransactoin) {
				((SmileTransactoin) t).commitRealy();
			} else {
				t.commit();
			}
		}
	}

	protected static void rollBackAllTransactions() throws SQLException {
		for (Transaction t : transactions.values()) {
			if (t instanceof SmileTransactoin) {
				((SmileTransactoin) t).connection.rollback();
			} else {
				t.rollback();
			}
		}
		if (logger.isDebugEnabled()) {
			logger.debug("rollback transaction " + transactions);
		}
	}

	protected static void closeAllTransactions() throws SQLException {
		try {
			for (Transaction t : transactions.values()) {
				if (t instanceof SmileTransactoin) {
					((SmileTransactoin) t).connection.close();
				} else {
					t.close();
				}
			}
		} finally {
			transactions.clear();
		}
	}
	/**
	 * 开启连接管理
	 * @param needCommit
	 */
	protected static void startManagered(boolean needCommit) {
		statusStack.synTransaction(needCommit);
	}
	
	protected static void endManagered() {
		statusStack.pop();
	}
	/**
	 * 是否已经开启事务
	 * @return
	 */
	public static boolean isBeginTransaction() {
		return statusStack.isBeginTransaction();
	}
	
	public static boolean isSynTransaction() {
		return statusStack.isSynTransaction();
	}
}
