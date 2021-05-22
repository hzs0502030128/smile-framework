package org.smile.orm;

public class OrmInitException extends RuntimeException {
	
	public OrmInitException(String msg,Throwable e){
		super(msg, e);
	}
	
	public OrmInitException(String msg){
		super(msg);
	}
}
