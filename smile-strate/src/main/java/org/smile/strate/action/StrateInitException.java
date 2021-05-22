package org.smile.strate.action;

import org.smile.commons.SmileRunException;

public class StrateInitException extends SmileRunException{
	public StrateInitException(String msg){
		super(msg);
	}
	
	public StrateInitException(String msg,Throwable e){
		super(msg,e);
	}
}
