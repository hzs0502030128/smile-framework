package org.smile.script;

/**
 * 代码执行器
 * @author 胡真山
 */
public interface Executor{
	/**
	 * 设置返回结果的变量名,不设置时系统会返回代码最后一行的变量值
	 * 
	 * @param var
	 */
	public void setResultVar(String var);
	/**
	 * 执行代码
	 * @param script
	 * @param beanParams
	 * @return
	 */
	public Object execute(String script,Object beanParams);
}
