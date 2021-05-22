package org.smile.orm.mapping;

import org.smile.commons.SmileRunException;

public class FieldPropertyException extends SmileRunException {

	public FieldPropertyException(String msg) {
		super(msg);
	}

	public FieldPropertyException(String msg,Throwable e) {
		super(msg,e);
	}
	
	public FieldPropertyException(Throwable e) {
		super(e);
	}
}
