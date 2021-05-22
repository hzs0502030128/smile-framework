package org.smile.report.excel;

import org.smile.util.RegExp;
/**
 * 表达式判断工具类
 * @author 胡真山
 *
 */
public class ExpressionUtils{
	/**
	 * $数据表达式
	 */
	protected static final RegExp _$Exp=new RegExp("^\\$\\{ *[_A-Za-z@\\.\\(\\)].* *\\}$");
	/**
	 * 动态数据配置正则
	 */
	protected static final RegExp dynamicReg=new RegExp("^%\\[ *[_A-Za-z0-9]+ *\\]$");
	/**
	 * 自定义函数表达式
	 */
	protected static final RegExp functionExpReg=new RegExp("^[_A-Za-z0-9\\$]+\\{ *[_A-Za-z].* *\\}$");
	/**常量表达式**/
	protected static final RegExp paramExpReg=new RegExp("^#[_A-Za-z\\$0-9]+$");
	/**内部变量表达式*/
	protected static final RegExp valibaleExpReg=new RegExp("^##[_A-Za-z\\$0-9]+$");
	/**
	 * 是否是表达式
	 * @param exp
	 * @return
	 */
	public static boolean is$Exp(String exp){
		return _$Exp.test(exp);
	}
	
	public static boolean isExp(String exp){
		if(is$Exp(exp)){
			return true;
		}
		if(functionExpReg.test(exp)){
			return true;
		}
		if(paramExpReg.test(exp)){
			return true;
		}
		if(valibaleExpReg.test(exp)){
			return true;
		}
		return false;
	}
	/**
	 * 表达式的名称
	 * @param exp
	 * @return
	 */
	public static String get$ExpName(String exp){
		exp=exp.trim();
		return exp.substring(2, exp.length()-1).trim();
	}
	/**
	 * 是否为动态表达式
	 * @param exp
	 * @return
	 */
	public static boolean isDynamicExp(String exp){
		return dynamicReg.test(exp);
	}
	/**
	 * 获取动态表达式名称
	 * @param exp
	 * @return
	 */
	public static String getDynamicExpName(String exp){
		return exp.substring(2, exp.length() - 1).trim();
	}
}
