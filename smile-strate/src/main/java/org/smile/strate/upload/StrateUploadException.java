package org.smile.strate.upload;

import org.smile.strate.StrateException;

public class StrateUploadException extends StrateException {
	
	public StrateUploadException(String msg,Throwable e){
		super(msg,e);
	}
	
	public StrateUploadException(Throwable e){
		super(e);
	}
}
