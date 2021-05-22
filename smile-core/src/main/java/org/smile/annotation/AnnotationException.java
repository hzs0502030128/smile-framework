package org.smile.annotation;

import org.smile.commons.SmileRunException;

public class AnnotationException extends SmileRunException{

	public AnnotationException(String msg) {
		super(msg);
	}
	
	public AnnotationException(Throwable e) {
		super(e);
	}
	
	public AnnotationException(String msg,Throwable e) {
		super(msg,e);
	}
}
