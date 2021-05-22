package org.smile.report.function;

import ognl.Ognl;
import ognl.OgnlException;

import org.smile.commons.Strings;
import org.smile.log.LoggerHandler;
/**
 * 对ognl支持的转换函数
 * @author 胡真山
 *
 */
public class OgnlFunction extends AbstractFunction implements LoggerHandler{
	
	public static OgnlFunction instance=new OgnlFunction();
	
	@Override
	public Object convert(Object oneData, String exp, Object expValue) {
		try {
			return Ognl.getValue(exp, oneData);
		} catch (OgnlException e) {
			logger.info("ognl 表达式"+exp+" error,dataobj "+oneData,e);
		}
		return Strings.BLANK;
	}

	@Override
	public boolean needFieldValue() {
		return false;
	}
	
}
