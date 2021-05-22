package org.smile.function;

import org.smile.util.StringUtils;


public class BasicFunctionParser implements FunctionParser{
	/**指令 开始标记*/
	protected char macroStart = '(';
	/** 指令结束标记*/
	protected char macroEnd = ')';
	
	public BasicFunctionParser(){}
	/**
	 * 函数解析的括号
	 * @param macroStart 起始括号
	 * @param macroEnd 结束括号
	 */
	public BasicFunctionParser(char macroStart,char macroEnd){
		this.macroStart=macroStart;
		this.macroEnd=macroEnd;
	}
	/**
	 * 解析成函数表达式信息
	 * 如果不符合函数表达式  返回null
	 * @param exp
	 * @return
	 */
	public BaseFunctionInfo parse(String exp){
		int sidx=exp.indexOf(macroStart);
		if(sidx>0){
			int eidx=exp.lastIndexOf(macroEnd);
			if(eidx>0){
				String fname=StringUtils.trim(exp.substring(0,sidx));
				String argsExp=StringUtils.trim(exp.substring(sidx+1,eidx));
				return createFunctionExpInfo(fname,argsExp);
			}
		}
		return null;
	}
	
	protected BaseFunctionInfo createFunctionExpInfo(String name,String exp){
		String[] args=exp.split(",");
		return new BaseFunctionInfo(name,args);
	}
}
