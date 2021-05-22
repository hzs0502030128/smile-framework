package net.sf.jsqlparser.expression;

import net.sf.jsqlparser.parser.SimpleNode;

public class SysConstansExpression implements Expression {

	private String name;
	@Override
	public void accept(ExpressionVisitor arg0) {

	}

	public SysConstansExpression(String name){
		this.name=name;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public SimpleNode getASTNode() {
		return null;
	}

	@Override
	public void setASTNode(SimpleNode node) {
		
	}
	
}
