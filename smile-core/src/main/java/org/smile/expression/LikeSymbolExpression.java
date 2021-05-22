package org.smile.expression;

import org.smile.expression.visitor.ExpressionVisitor;
import org.smile.expression.visitor.SymbolExpressionVisitor;
import org.smile.util.RegExp;
/**
 * 模糊匹配操作符
 * @author 胡真山
 *
 */
public class LikeSymbolExpression extends SymbolExpression {
	//like匹配正则表达式
	private RegExp likeReg;
	
	public LikeSymbolExpression(String operate){
		this.operate=operate;
	}

	@Override
	public Object evaluate(Context root, Expression left, Expression right) {
		if(likeReg==null){
			this.likeReg=createRegExp(String.valueOf(right.evaluate(root)));
		}
		return likeReg.matches(String.valueOf(left.evaluate(root)));
	}
	
	/**创建like正则表达式*/
	private RegExp createRegExp(Object obj) {
		String str = String.valueOf(obj);
		str = str.replaceAll("%", ".*");
		str = str.replaceAll("_", ".");
		RegExp reg = new RegExp(str);
		return reg;
	}
	
	@Override
	public void accept(SymbolExpressionVisitor visitor) {
		visitor.visit(this);
	}
}
