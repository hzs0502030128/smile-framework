package org.smile.expression;
/**
 * 转为字符串是如果有前缀会形成 #{name}样式
 * 特殊参数表达式
 * 可以用于封装 ?  
 * @author 胡真山
 *
 */
public class SpecialParameterExpression extends ParameterNameExpression{
	/**表达式后后缀*/
	protected String suffix="";
	
	public SpecialParameterExpression(String name) {
		this.prefix="";
		this.name=name;
		this.suffix="";
	}
	/**
	 * 
	 * @param flag 标记符号
	 * @param name 参数名称
	 */
	public SpecialParameterExpression(String flag,String name) {
		this.name=name;
		this.prefix=flag+"{";
		this.suffix="}";
	}
	
	@Override
	public Object evaluate(Context root) {
		return root.getParameter(this.prefix);
	}

	@Override
	public String toString() {
		return this.prefix+this.name+this.suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

}
