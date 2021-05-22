package org.smile.commons;


public class SmileException extends Exception {
	
	/**保存下一级的cause*/
	protected Throwable subCause;
	
	public SmileException(){
		super("smile exception");
	}
	
	public SmileException(String message,Throwable cause) {
		super(message,cause);
	}
	
	public SmileException(Throwable cause) {
		super("smile exception", cause);
		subCause=cause.getCause();
	}
	
	public SmileException(String message) {
		super(message);
	}
	
	@Override
	public synchronized Throwable getCause() {
		Throwable cause=super.getCause();
		if(cause==null){
			return subCause;
		}
		return cause;
	}
}
