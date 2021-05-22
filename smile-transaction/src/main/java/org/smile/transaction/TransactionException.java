package org.smile.transaction;

public class TransactionException extends Exception{
	
	public TransactionException(String msg,Throwable e){
		super(msg,e);
	}
	
	public TransactionException(Throwable e){
		super(e);
	}
}
