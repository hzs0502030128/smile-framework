package org.smile.db;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.smile.db.sql.BasicTransaction;

public class TransactionHandler {
	
	public Transaction getTransaction(DataSource dataSource) throws SQLException{
		return new BasicTransaction(dataSource);
	}
}
