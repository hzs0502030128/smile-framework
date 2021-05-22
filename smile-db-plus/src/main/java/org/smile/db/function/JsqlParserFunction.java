package org.smile.db.function;


public class JsqlParserFunction implements SqlFunction {

	private net.sf.jsqlparser.expression.Function function;
	
	public JsqlParserFunction(net.sf.jsqlparser.expression.Function f){
		this.function=f;
	}

	@Override
	public String getName() {
		return function.getName();
	}
	
	@Override
	public Object getAdapted() {
		return function;
	}

}
