package org.smile.strate;

import javax.servlet.ServletException;

public class StrateException extends ServletException {
	/**保存下一级的cause*/
	protected Throwable subCause;
	
	public StrateException(String msg,Throwable e){
		super(msg,e);
		subCause=e.getCause();
	}
	
	public StrateException(String msg){
		super(msg);
	}
	
	public StrateException(Throwable e){
		super(e);
		this.subCause=e.getCause();
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
