package org.smile.report.excel;
/**
 * 读取注解配置信息失败
 * @author 胡真山
 *
 */
public class CommentConfigReadException extends RuntimeException {
	public CommentConfigReadException(String msg,Throwable e){
		super(msg, e);
	}
}
