package org.smile.expression;

import org.smile.commons.Strings;
import org.smile.expression.visitor.SymbolExpressionVisitor;
import org.smile.util.StringUtils;
/**
 * 条件扣操作符表达式
 * @author 胡真山
 *
 */
public class ConditionSymbolExpression extends SymbolExpression {
	
	public ConditionSymbolExpression(String oprate){
		this.operate=StringUtils.remove(StringUtils.trim(oprate),Strings.SPACE);
	}

	@Override
	public Object evaluate(Context root,Expression left, Expression right) {
		String flag=this.evaluate(root);
		if (AND.equals(flag)) {
			return (Boolean)left.evaluate(root) &&(Boolean)right.evaluate(root);
		} else if (OR.equals(flag)) {
			return (Boolean)left.evaluate(root) ||(Boolean)right.evaluate(root);
		}
		throw new EvaluateException("not support "+flag);
	}
	
	@Override
	public void accept(SymbolExpressionVisitor visitor) {
		visitor.visit(this);
	}
}
