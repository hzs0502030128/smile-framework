package org.smile.db.spring;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.smile.db.Transaction;
import org.smile.db.TransactionHandler;

public class SpringTransactionHandler extends TransactionHandler{
	@Override
	public Transaction getTransaction(DataSource dataSource) throws SQLException {
		return new SpringTransaction(dataSource);
	}
}
