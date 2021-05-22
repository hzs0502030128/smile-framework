package org.smile.commons;


public class SmileRunException extends RuntimeException {
	/**保存下一级的cause*/
	protected Throwable subCause;
	
	public SmileRunException(Throwable e){
		super(e);
		subCause=e.getCause();
	}
	
	public SmileRunException(String msg,Throwable e){
		super(msg,e);
		subCause=e.getCause();
	}
	
	public SmileRunException(String msg){
		super(msg);
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
