package org.smile.db.sql.parameter.function;

import java.util.Date;

import org.smile.commons.SmileRunException;
import org.smile.function.CheckArgsCountFunction;
import org.smile.util.DateUtils;
/**
 * 开始日期参数 
 * 合把日期格式化成 yyyy-MM-dd
 * @author 胡真山
 *
 */
public class StartDateParameterFunction extends  CheckArgsCountFunction{

	@Override
	public Object getFunctionValue(Object... args) {
		checkArgs(args);
		Object param=args[0];
		if(param instanceof Date){
			return DateUtils.formatOnlyDate((Date)param);
		}else if(param instanceof String){
			Date date=DateUtils.parseDate((String)param);
			return DateUtils.formatOnlyDate(date);
		}
		throw new SmileRunException(param +" can to convert to date ");
	}

	@Override
	public String getName() {
		return "startdate";
	}

	@Override
	public int getSupportArgsCount() {
		return 1;
	}

}
