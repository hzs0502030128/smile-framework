package org.smile.db.sql.parameter;

import org.smile.commons.StringBand;
import org.smile.function.Function;

/***
 * 用来封装一个键值对 保存参数名与参数值
 * @author 胡真山
 */
public class Parameter {
	/**
	 * 参数名
	 */
	private String name;
	/**
	 * 参数值
	 */
	private Object value;
	/**
	 * 参数的函数
	 */
	private Function function;
	
	
	public Parameter(String name,Object value){
		this.name=name;
		this.value=value;
	}

	public String getName() {
		return name;
	}

	public Object getValue() {
		return value;
	}

	public Function getFunction() {
		return function;
	}

	public void setFunction(Function function) {
		this.function = function;
	}
	
	@Override
	public String toString(){
		StringBand string=new StringBand("{");
		if(function!=null){
			string.append(function.getName()).append("(").append(name).append(")");
			string.append(":").append(value);
		}else{
			string.append(name).append(":").append(value);
		}
		string.append("}");
		return string.toString();
	}
}
