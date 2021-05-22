package org.smile.transaction;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.smile.db.Transaction;
import org.smile.db.TransactionHandler;

public class SmileTransactionHandler extends TransactionHandler{

	@Override
	public Transaction getTransaction(DataSource dataSource)
			throws SQLException {
		return TransactionUtils.getTransaction(dataSource);
	}

}
