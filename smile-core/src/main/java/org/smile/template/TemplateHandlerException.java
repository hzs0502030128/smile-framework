package org.smile.template;

public class TemplateHandlerException extends RuntimeException {
	public TemplateHandlerException(String msg,Throwable e){
		super(msg,e);
	}
	
	public TemplateHandlerException(String msg){
		super(msg);
	}
	
	public TemplateHandlerException(Throwable e){
		super(e);
	}
}
