package org.smile.expression;



/**
 * 赋值操作符表达式
 * @author 胡真山
 *
 */
public class VoluationSymbolExpression extends MathSymbolExpression{
	
	public VoluationSymbolExpression(){
		this.operate="=";
	}

	@Override
	public Object evaluate(Context root, Expression left, Expression right) {
		if(left instanceof FieldNameExpression){
			String name=((FieldNameExpression) left).getName();
			Object value= right.evaluate(root);
			root.set(name,value);
			return value;
		}else if(left instanceof ParameterNameExpression){
			String name=((ParameterNameExpression) left).getName();
			Object value= right.evaluate(root);
			root.setParameter(name,value);
			return value;
		}
		throw notSupportOperateException(left, right);
	}
	
}
