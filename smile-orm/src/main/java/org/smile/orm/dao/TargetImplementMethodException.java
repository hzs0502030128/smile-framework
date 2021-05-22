package org.smile.orm.dao;

public class TargetImplementMethodException extends RuntimeException{
	
	public TargetImplementMethodException(String msg,Throwable e){
		super(msg, e);
	}
	
	public TargetImplementMethodException(String msg){
		super(msg);
	}
}
