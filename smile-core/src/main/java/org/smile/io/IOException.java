package org.smile.io;

public class IOException extends java.io.IOException{
	/**保存下一级的cause*/
	protected Throwable subCause;
	
	public IOException(Throwable e){
		super(e);
		subCause=e.getCause();
	}
	
	public IOException(String msg,Throwable e){
		super(msg,e);
		subCause=e.getCause();
	}
	
	public IOException(String msg){
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
