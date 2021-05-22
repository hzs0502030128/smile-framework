package org.smile.tag.impl;

import org.smile.commons.SmileException;

public class TagException extends SmileException{
	
	public TagException(String message,Throwable cause) {
		super(message,cause);
	}
	
	public TagException(Throwable cause) {
		super("tag exception", cause);
	}
	
	public TagException(String cause) {
		super(cause);
	}
}
