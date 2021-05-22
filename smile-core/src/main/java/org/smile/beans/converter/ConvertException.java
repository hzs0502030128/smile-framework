package org.smile.beans.converter;


public class ConvertException extends BeanException{
	
	public ConvertException(String message,Throwable cause) {
		super(message,cause);
	}
	public ConvertException(Throwable cause) {
		super("type convert exception "+cause.getMessage(),cause);
	}
	public ConvertException(String message) {
		super("type convert exception: "+message);
	}
}
