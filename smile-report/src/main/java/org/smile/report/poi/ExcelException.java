package org.smile.report.poi;

import org.smile.commons.SmileRunException;
/**
 * 
 * @author 胡真山
 *
 */
public class ExcelException extends SmileRunException {

	public ExcelException(String msg, Throwable e) {
		super(msg, e);
	}
	
	public ExcelException(String msg) {
		super(msg);
	}
	
	public ExcelException(Throwable e){
		super("operator excel exception ", e);
	}
}
