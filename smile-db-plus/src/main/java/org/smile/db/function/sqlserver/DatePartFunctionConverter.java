package org.smile.db.function.sqlserver;

import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.SysConstansExpression;

import org.smile.db.function.JsqlParserFunctionConverter;

public class DatePartFunctionConverter  extends JsqlParserFunctionConverter{

	private String type;
	public DatePartFunctionConverter(String type){
		this.type=type;
	}
	@Override
	protected void doConvert(Function f) {
		f.setName("DatePart");
		f.getParameters().getExpressions().add(0,new SysConstansExpression(type));
	}


}
