package org.smile.db.function.oracle;

import net.sf.jsqlparser.expression.Function;

import org.smile.db.function.JsqlParserFunctionConverter;

public class GroupConcatFunctionConverter extends JsqlParserFunctionConverter {

	@Override
	protected void doConvert(Function f) {
		f.setName("wm_concat");
	}

}
