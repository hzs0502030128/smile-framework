package org.smile.db;

import org.smile.commons.SmileRunException;

/**
 * 数据库操作的运行时异常
 * @author 胡真山
 * @Date 2016年4月8日
 */
public class SqlRunException extends SmileRunException {
	
	public SqlRunException(Throwable e){
		super(e);
	}
	
	public SqlRunException(String msg,Throwable e){
		super(msg,e);
	}
	
	public SqlRunException(String msg){
		super(msg);
	}
}
