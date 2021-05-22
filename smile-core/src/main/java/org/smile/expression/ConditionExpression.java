package org.smile.expression;

import org.smile.collection.ExpressionFilter;
import org.smile.expression.visitor.ExpressionVisitor;

/**
 * 查询条件 这是一个用于对象使用类sql语句筛选的条件
 * 例如：
 * "not (name like '胡_3') and ((name <'胡5' or not name like '胡2%') and name <> '胡25')"
 * 参考{@link ExpressionFilter}使用
 * 
 * */
public class ConditionExpression extends BinaryExpression<Boolean>{

	public ConditionExpression(Expression one, Expression two, SymbolExpression oprate) {
		super(oprate);
		this.left = one;
		this.right=two;
	}

	/**
	 * 计算结果为一个boolean值
	 * @param root
	 * @return
	 */
	protected Boolean evaluateBoolean(Context root){
		return (Boolean)this.operate.evaluate(root, left, right);
	}
	

	@Override
	public Boolean evaluate(Context root) {
		boolean result=false;
		if(operate==null){
			result= (Boolean)left.evaluate(root);
		}else {
			result= evaluateBoolean(root);
		}
		return not^(Boolean)result;
	}
	
	@Override
	public void accept(ExpressionVisitor visitor) {
		visitor.visit(this);
	}
}
