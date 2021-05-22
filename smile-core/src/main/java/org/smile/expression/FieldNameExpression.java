package org.smile.expression;

import org.smile.expression.visitor.NamedExpressionVisitor;

/**
 * 字段名称对象 用于在解析语句后标记为对象属性字段
 * @author 胡真山
 */
public class FieldNameExpression extends NamedExpression {
	
	public FieldNameExpression(String name){
		this.name=convertKeyToProperty(name);
	}

	@Override
	public Object evaluate(Context root) {
		if(root==null){
			throw new EvaluateException("root context is null for get field '"+name+"'");
		}
		return root.get(name);
	}

	@Override
	public  void accept(NamedExpressionVisitor visitor) {
		visitor.visit(this);
	}
	
}
