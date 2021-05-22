package org.smile.ioc.load;

public class IocContextLoadException extends RuntimeException {
	public IocContextLoadException(String msg,Throwable e){
		super(msg, e);
	}
}
