package org.smile.db.function;

import java.util.Map;
import java.util.Set;

import org.smile.collection.KeyNoCaseHashMap;
import org.smile.commons.ExceptionUtils;
import org.smile.db.function.oracle.CeilingFunctionConverter;
import org.smile.db.function.oracle.GroupConcatFunctionConverter;
import org.smile.db.function.oracle.IfnullFunctionConverter;
import org.smile.db.function.oracle.SubstringFunctionConverter;


public class OracleFunctionHandler implements FunctionHandler {

	protected static Map<String,FunctionConverter> converters=new KeyNoCaseHashMap<FunctionConverter>();
	
	static{
		try{
			converters.put("ifnull", new IfnullFunctionConverter());
			converters.put("substring", new SubstringFunctionConverter());
			converters.put("groupconcat", new GroupConcatFunctionConverter());
			converters.put("ceiling", new CeilingFunctionConverter());
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
