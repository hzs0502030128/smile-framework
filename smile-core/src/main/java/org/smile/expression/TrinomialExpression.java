package org.smile.expression;

import java.util.Set;

import org.smile.collection.NoCaseStringSet;
import org.smile.commons.Strings;
import org.smile.expression.visitor.ExpressionVisitor;

/**
 * 三目运算表达式
 * @author 胡真山
 *
 */
public class TrinomialExpression extends  SingleExpression<Object>{
	/**可认为是true的字符串*/
	protected static Set<String> TrueValue=new NoCaseStringSet("Y","YES","TRUE","OK");
	/**可认为是false的字符串*/
	protected static Set<String> FalseValue=new NoCaseStringSet("N","NO","FALSE");

	/**右边表达式*/
	protected Expression right;
	/**
	 * 条件表达式
	 */
	protected Expression condition;
	
	@Override
	public Object evaluate(Context root) {
		Object conditionResult=condition.evaluate(root);
		Boolean booleanRes=null;
		if(conditionResult instanceof Boolean){
			booleanRes=(Boolean)conditionResult;
		}else if(conditionResult instanceof String){
			if(TrueValue.contains(conditionResult)){
				booleanRes=true;
			}else if(FalseValue.contains(conditionResult)){
				booleanRes=false;
			}
		}
		if(booleanRes==null){
			throw new EvaluateException(condition+" 不能转换成布尔类型 ");
		}
		if(booleanRes){
			return left.evaluate(root);
		}else{
			return right.evaluate(root);
		}
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		if(not){
			str.append(" NOT ");
		}
		str.append("(");
		append(condition,str);
		str.append(Strings.SPACE).append(Strings.QUESTION_MARK).append(Strings.SPACE);
		append(left,str);
		str.append(Strings.SPACE);
		str.append(Strings.COLON);
		str.append(Strings.SPACE);
		append(right,str);
		str.append(")");
		return str.toString();
	}

	public Expression getRight() {
		return right;
	}

	public void setRight(Expression right) {
		this.right = right;
	}

	public Expression getCondition() {
		return condition;
	}

	public void setCondition(Expression condition) {
		this.condition = condition;
	}
	
	@Override
	public void accept(ExpressionVisitor visitor) {
		visitor.visit(this);
	}
}
