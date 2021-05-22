package org.smile.db.function.mysql;

import java.util.List;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;

import org.smile.db.function.JsqlParserFunctionConverter;

public class ToCharFunctionConverter extends JsqlParserFunctionConverter{
	
	private String getFormat(String f){
		String ftxt=MySqlDateFormat.getFormat(f);
		if(ftxt!=null){
			return ftxt;
		}
		return f;
	}
	@Override
	protected void doConvert(Function f) {
		ExpressionList list=f.getParameters();
		List<Expression> expressions=list.getExpressions();
		if(expressions.size()>1){
			Expression exp=expressions.get(1);
			f.setName("date_format");
			if(exp instanceof StringValue){
				StringValue value=((StringValue) exp);
				value.setValue(getFormat(value.getValue()));
			}
		}
	}

}
