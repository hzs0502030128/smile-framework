package org.smile.orm.xml;

import org.smile.commons.SmileRunException;

/**
 * dao xml 文件加载异常
 * @author 胡真山
 * 2015年11月6日
 */
public class DaoXmlLoadException extends SmileRunException {
	
	public DaoXmlLoadException(String msg,Throwable e){
		super(msg, e);
	}
}
