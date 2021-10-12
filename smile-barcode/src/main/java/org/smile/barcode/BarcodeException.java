package org.smile.barcode;

public class BarcodeException extends Exception {
	public BarcodeException(Throwable e){
		super(e);
	}
	
	public BarcodeException(String message,Throwable e){
		super(message,e);
	}
	
	public BarcodeException(String message){
		super(message);
	}
}
