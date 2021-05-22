package org.smile.expression;

import org.smile.commons.Strings;
import org.smile.expression.visitor.ExpressionVisitor;
import org.smile.expression.visitor.NamedExpressionVisitor;
import org.smile.util.RegExp;


public abstract class NamedExpression extends SimpleExpression<Object> {
	protected static RegExp paramExpStart=new RegExp("\\[");
	protected static RegExp paramExpEnd=new RegExp("\\]+");
	protected String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	@Override
	public String toString() {
		return name;
	}
	
	protected String convertKeyToProperty(String name){
		name=paramExpStart.replaceAll(name, Strings.DOT);
		return paramExpEnd.replaceAll(name, Strings.BLANK);
	}
	
	@Override
	public  void accept(ExpressionVisitor visitor) {
		visitor.visit(this);
	}
	
	public abstract void accept(NamedExpressionVisitor visitor);
	
}
