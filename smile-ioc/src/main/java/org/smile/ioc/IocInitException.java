package org.smile.ioc;

import org.smile.commons.SmileRunException;

public class IocInitException extends SmileRunException{

	public IocInitException(String msg, Throwable e) {
		super(msg, e);
	}
	
	public IocInitException(Throwable e) {
		super(e);
	}
	
	public IocInitException(String msg) {
		super(msg);
	}
}
