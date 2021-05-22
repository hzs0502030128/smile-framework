package org.smile.function;
/**
 * 函数表达式信息
 * @author 胡真山
 *
 */
public class BaseFunctionInfo implements FunctionAware<String>{
	/**函数名称*/
	private String name;
	/**参数表达式**/
	private String[] expression;
	
	public BaseFunctionInfo(String name,String[] exp){
		this.name=name;
		this.expression=exp;
	}

	@Override
	public String[] getArgExpression() {
		return this.expression;
	}

	@Override
	public String getName() {
		return name;
	}
}
