package org.smile.db.function;

public abstract class JsqlParserFunctionConverter implements FunctionConverter{

	@Override
	public void convert(SqlFunction f) {
		net.sf.jsqlparser.expression.Function function=(net.sf.jsqlparser.expression.Function)f.getAdapted();
		doConvert(function);
	}
	
	protected abstract void doConvert(net.sf.jsqlparser.expression.Function f);
	
}
