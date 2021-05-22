package org.smile.db.function;

import java.util.Map;
import java.util.Set;

import org.smile.collection.KeyNoCaseHashMap;
import org.smile.commons.ExceptionUtils;
import org.smile.db.function.sqlserver.DatePartFunctionConverter;
import org.smile.db.function.sqlserver.IfnullFunctionConverter;
import org.smile.db.function.sqlserver.LengthFunctionConverter;
import org.smile.db.function.sqlserver.RoundFunctionConverter;
import org.smile.db.function.sqlserver.SysdateFunctionConverter;
import org.smile.db.function.sqlserver.ToCharFunctionConverter;
import org.smile.db.function.sqlserver.ToDateFunctionConverter;


public class SqlServerFunctionHandler implements FunctionHandler {

	protected static Map<String,FunctionConverter> converters=new KeyNoCaseHashMap<FunctionConverter>();
	
	static{
		try{
			converters.put("to_char", new ToCharFunctionConverter());
			converters.put("to_date", new ToDateFunctionConverter());
			converters.put("round", new RoundFunctionConverter());
			converters.put("sysdate", new SysdateFunctionConverter());
			converters.put("ifnull", new IfnullFunctionConverter());
			converters.put("hour", new DatePartFunctionConverter("hour"));
			converters.put("minute", new DatePartFunctionConverter("minute"));
			converters.put("second", new DatePartFunctionConverter("second"));
			converters.put("week", new DatePartFunctionConverter("week"));
			converters.put("weekday", new DatePartFunctionConverter("weekday"));
			converters.put("length", new LengthFunctionConverter());
		}catch(Throwable e){
			logger.info("函数转换功能初始化失败:"+ExceptionUtils.getExceptionMsg(e));
		}
	}
	
	@Override
	public void convertFuction(SqlFunction function) {
		String functionName=function.getName();
		FunctionConverter converter=converters.get(functionName);
		if(converter!=null){
			converter.convert(function);
		}
	}

	@Override
	public Set<String> viewSupport() {
		return converters.keySet();
	}

}
