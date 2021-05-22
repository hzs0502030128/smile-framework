package org.smile.expression;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.smile.expression.visitor.ExpressionVisitor;
import org.smile.expression.visitor.SymbolExpressionVisitor;
import org.smile.util.StringUtils;

/**
 * instanceof
 * @author 胡真山
 *
 */
public class InstanceofSymbolExpression extends SymbolExpression {
	
	private static final Map<String,Class> CUSTOM_TYPE=new HashMap<String,Class>();
	static{
		CUSTOM_TYPE.put("map", Map.class);
		CUSTOM_TYPE.put("list",List.class);
		CUSTOM_TYPE.put("set",Set.class);
		CUSTOM_TYPE.put("collection",Collection.class);
		CUSTOM_TYPE.put("date",Date.class);
		CUSTOM_TYPE.put("string",String.class);
		CUSTOM_TYPE.put("int",Integer.class);
		CUSTOM_TYPE.put("double",Double.class);
		CUSTOM_TYPE.put("long",Long.class);
		CUSTOM_TYPE.put("byte",Byte.class);
		CUSTOM_TYPE.put("float",Float.class);
	}
	
	public InstanceofSymbolExpression(String operate){
		this.operate=StringUtils.trim(operate);
	}

	@Override
	public Object evaluate(Context root, Expression left, Expression right) {
		Object leftValue=left.evaluate(root);
		Class type=null;
		if(leftValue == null ){
			return false;
		}else if(right instanceof FieldNameExpression){
			String classType=((FieldNameExpression) right).getName();
			type=parseClass(classType);
		}else if(right instanceof ParameterNameExpression){
			Object rightValue=right.evaluate(root);
			if(rightValue instanceof Class){
				type=(Class) rightValue;
			}else{
				type=parseClass(rightValue.toString());
			}
		}
		return type.isAssignableFrom(leftValue.getClass());
	}
	
	private Class parseClass(String clazz){
		try {
			Class resType=CUSTOM_TYPE.get(clazz);
			if(resType!=null){
				return resType;
			}
			return Class.forName(clazz);
		} catch (ClassNotFoundException e) {
			throw new EvaluateException(this.operate,e);
		}
	}
	
	@Override
	public void accept(SymbolExpressionVisitor visitor) {
		visitor.visit(this);
	}
}
