package org.smile.db.function.sqlserver;

import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.SysConstansExpression;

import org.smile.db.function.JsqlParserFunctionConverter;

public class ToDateFunctionConverter extends JsqlParserFunctionConverter{

	@Override
	protected void doConvert(Function f) {
		f.setName("convert");
		f.getParameters().getExpressions().add(0, new SysConstansExpression("datetime"));
	}

}
