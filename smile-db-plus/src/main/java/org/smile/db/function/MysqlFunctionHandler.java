package org.smile.db.function;

import java.util.Map;
import java.util.Set;

import org.smile.collection.KeyNoCaseHashMap;
import org.smile.commons.ExceptionUtils;
import org.smile.db.function.mysql.LengthFunctionConverter;
import org.smile.db.function.mysql.ToCharFunctionConverter;


public class MysqlFunctionHandler implements FunctionHandler {

	protected static Map<String,FunctionConverter> converters=new KeyNoCaseHashMap<FunctionConverter>();
	
	static{
		try{
			converters.put("to_char", new ToCharFunctionConverter());
			converters.put("len", new LengthFunctionConverter());
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
