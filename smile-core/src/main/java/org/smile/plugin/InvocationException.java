package org.smile.plugin;

import org.smile.commons.SmileRunException;

public class InvocationException extends SmileRunException{

	public InvocationException(String msg, Throwable e) {
		super(msg, e);
	}
	
	public InvocationException(Throwable e) {
		super(e);
	}

}
