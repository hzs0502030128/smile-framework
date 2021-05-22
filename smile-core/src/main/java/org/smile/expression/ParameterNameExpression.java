package org.smile.expression;

import org.smile.expression.visitor.NamedExpressionVisitor;

public class ParameterNameExpression extends NamedExpression{
	
	protected String prefix="#";
	
	protected ParameterNameExpression() {}
	
	public ParameterNameExpression(String name){
		this.name=name.substring(1);
	}
	@Override
	public String toString(){
		return prefix+name;
	}

	@Override
	public Object evaluate(Context root) {
		return root.getParameter(name);
	}
	/**
	 * 设置前缀
	 * @param prefix
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
	@Override
	public void accept(NamedExpressionVisitor visitor) {
		visitor.visit(this);
	}
	
}
