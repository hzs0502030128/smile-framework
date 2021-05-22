package org.smile.expression;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.smile.commons.StringBand;
import org.smile.expression.visitor.ExpressionVisitor;

/**
 * 多个表达式的集合 用 ; 分开
 * @author 胡真山
 *
 */
public class MultipleExpression extends SimpleExpression<Object>{
	/**多表达式的的集合*/
	private List<Expression> expressions=new LinkedList<Expression>();
	/**
	 * 添加表达式
	 * @param e
	 */
	public void addExpression(Expression e){
		this.expressions.add(e);
	}
	/**
	 * 添加表达式到指定的索引位置
	 * @param index
	 * @param e
	 */
	public void addExpression(int index,Expression e){
		this.expressions.add(index, e);
	}

	@Override
	public Object evaluate(Context root) {
		Object res=null;
		for(Expression e:expressions){
			res=e.evaluate(root);
		}
		return res;
	}

	@Override
	public String toString() {
		StringBand res=new StringBand();
		int i=0;
		for(Expression e:expressions){
			if(i++>0){
				res.append(";");
			}
			res.append(e);
		}
		return res.toString();
	}
	
	public Iterator<Expression> iterator(){
		return expressions.iterator();
	}
	
	@Override
	public void accept(ExpressionVisitor visitor) {
		visitor.visit(this);
	}
	
}
