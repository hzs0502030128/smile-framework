package org.smile.db.sql.parameter.function;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.smile.function.Function;
/**
 * 系统参数函数集
 * @author 胡真山
 */
public class ParameterFunctionContext {
	
	public static ParameterFunctionContext instance=new ParameterFunctionContext();
	
	private Map<String,Function> functionMap=new ConcurrentHashMap<String,Function>();
	
	private ParameterFunctionContext(){
		registFunction(new LikeParameterFunction());
		registFunction(new LeftLikeParameterFunction());
		registFunction(new RightLikeParameterFunction());
		registFunction(new StartDateParameterFunction());
		registFunction(new EndDateParameterFunction());
	}
	/**
	 * 获取获取
	 * @param name 函数名称
	 * @return 函数实现类实例
	 */
	public Function getFunction(String name){
		return functionMap.get(name);
	}
	/**
	 * 注册一个函数
	 * @param function
	 */
	public void registFunction(Function function){
		functionMap.put(function.getName(), function);
	}
	
}
