package org.smile.io.buff;
/**
 * 操作缓冲数据异常
 * @author 胡真山
 * 2015年11月17日
 */
public class BufferException extends RuntimeException {
	
	public BufferException(Throwable e){
		super(e);
	}
	
	public BufferException(String msg,Throwable e){
		super(msg,e);
	}
	
}
