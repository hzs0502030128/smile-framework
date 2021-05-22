package org.smile.db.function.sqlserver;

import net.sf.jsqlparser.expression.Function;

import org.smile.db.function.JsqlParserFunctionConverter;

public class LengthFunctionConverter extends JsqlParserFunctionConverter {

	@Override
	protected void doConvert(Function f) {
		f.setName("len");
	}


}
