package org.smile.gateway;

public class GatewayException extends RuntimeException{
	public GatewayException(String msg){
		super(msg);
	}
	
	public GatewayException(String msg,Throwable e){
		super(msg, e);
	}
}
