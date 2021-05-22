package org.smile.io.serialize;
/**
 * 对象序列化时抛出异常
 * @author 胡真山
 *
 */
public class SerializeException extends RuntimeException{
	
	public SerializeException(String msg,Throwable e){
		super(msg, e);
	}
	
	public SerializeException(String msg){
		super(msg);
	}
	
	public SerializeException(Throwable e){
		super(e);
	}
}
