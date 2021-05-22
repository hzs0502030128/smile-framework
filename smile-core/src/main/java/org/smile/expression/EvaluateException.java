package org.smile.expression;

import org.smile.commons.SmileRunException;

public class EvaluateException extends SmileRunException{

	public EvaluateException(String msg) {
		super(msg);
	}
	
	public EvaluateException(String msg,Throwable e) {
		super(msg,e);
	}

}
