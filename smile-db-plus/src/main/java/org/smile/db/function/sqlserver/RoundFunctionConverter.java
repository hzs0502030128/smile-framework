package org.smile.db.function.sqlserver;

import java.util.List;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.LongValue;

import org.smile.db.function.JsqlParserFunctionConverter;

public class RoundFunctionConverter extends JsqlParserFunctionConverter {

	@Override
	protected void doConvert(Function f) {
		List<Expression> list=f.getParameters().getExpressions();
		if(list.size()==1){
			list.add(new LongValue(0));
		}
	}

}
