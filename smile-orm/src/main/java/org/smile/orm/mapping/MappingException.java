package org.smile.orm.mapping;

public class MappingException extends Exception {
	public MappingException(String msg){
		super(msg);
	}
	public MappingException(String msg,Throwable e){
		super(msg,e);
	}
}
