package org.smile.strate.jump;

import org.smile.strate.StrateException;

public class StrateResultJumpException extends StrateException {

	public StrateResultJumpException(String msg) {
		super(msg);
	}
	
	public StrateResultJumpException(String msg,Throwable e) {
		super(msg,e);
	}
	
	public StrateResultJumpException(Throwable e) {
		super(e);
	}

}
