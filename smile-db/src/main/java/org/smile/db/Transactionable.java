package org.smile.db;

public interface Transactionable {
	
	public Transaction getTransaction();
	
	public void setTransaction(Transaction transaction);
}
