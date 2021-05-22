package org.smile.db.sql.parameter;

import org.smile.commons.SmileRunException;
import org.smile.db.sql.parameter.function.ParameterFunctionContext;
import org.smile.function.Function;
import org.smile.util.StringUtils;
/**
 * 通过对表达式 使用:方式分开函数名与属性表达式
 * 如：  like:name  则是用like函数去转name属性的值     结果  为  %name%
 *   age 则是普通参数
 * @author 胡真山
 *
 */
public class ParameterBuilder {
	/**表达式*/
	private String parameterName;
	/**函数名称*/
	private String functionName;
	
	/**
	 * @param exp 占位参数表达式
	 * 
	 * 如  普通参数 name 
	 *   函数参数 like:name  
	 */
	public ParameterBuilder(String exp){
		String[] args=StringUtils.split(exp, ":");
		if(args.length==2){
			functionName=args[0];
			parameterName=args[1];
		}else{
			parameterName=exp;
		}
	}
	
	/**
	 * 参数名称
	 * @return
	 */
	public String getParameterName(){
		return this.parameterName;
	}
	/**
	 * 是否是一个函数参数
	 * @return
	 */
	public boolean isFunction(){
		return functionName!=null;
	}
	
	/**
	 * 获取函数实现
	 * @return
	 */
	public Function getFunction(){
		Function function =ParameterFunctionContext.instance.getFunction(functionName);
		if(function==null){
			throw new SmileRunException("不支持的函数名:"+functionName);
		}
		return function;
	}
	
}
