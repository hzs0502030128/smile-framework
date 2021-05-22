package org.smile.db.function.oracle;

import net.sf.jsqlparser.expression.Function;

import org.smile.db.function.JsqlParserFunctionConverter;

public class SubstringFunctionConverter extends JsqlParserFunctionConverter {

	@Override
	protected void doConvert(Function f) {
		f.setName("substr");
	}

}
