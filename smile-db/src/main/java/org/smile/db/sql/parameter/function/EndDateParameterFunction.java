package org.smile.db.sql.parameter.function;

import java.util.Date;

import org.smile.commons.SmileRunException;
import org.smile.function.CheckArgsCountFunction;
import org.smile.util.DateUtils;
/**
 * 结束日期参数
 * 合把日期格式化成 yyyy-MM-dd 23:59:59
 * @author 胡真山
 *
 */
public class EndDateParameterFunction extends CheckArgsCountFunction{

	@Override
	public Object getFunctionValue(Object... args) {
		checkArgs(args);
		Object arg=args[0];
		if( arg instanceof Date){
			return DateUtils.formatOnlyDate((Date) arg)+" 23:59:59";
		}else if( arg instanceof String){
			Date date=DateUtils.parseDate((String) arg);
			return DateUtils.formatOnlyDate(date)+" 23:59:59";
		}
		throw new SmileRunException(args +" can to convert to date ");
	}

	@Override
	public String getName(){
		return "enddate";
	}
	
	@Override
	public int getSupportArgsCount() {
		return 1;
	}
	
}
