package org.smile.template.handler;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.smile.expression.Context;
import org.smile.expression.Engine;
import org.smile.expression.ExpressionEngine;
import org.smile.function.Function;
import org.smile.function.FunctionWrap;
import org.smile.template.StringTemplateParser.BaseMacroResolver;
import org.smile.template.TemplateParser;

public class WrapSimpleTemplateHandler extends SimpleTemplateHandler implements FunctionWrap{
	/**
	 * 函数信息
	 */
	private Map<String, Function> functionMap=new HashMap<String, Function>();
	
	public WrapSimpleTemplateHandler(String templateTxt) {
		super(templateTxt);
	}

	public WrapSimpleTemplateHandler(TemplateParser parser, String templateTxt) {
		super(parser,templateTxt);
	}

	@Override
	public String processToString(Object params) {
		final Engine engine=Engine.getInstance();
		return processToString(engine, params);
	}
	
	public String processToString(final ExpressionEngine engine,Object params) {
		Context context=null;
		if(params instanceof Context){
			context=(Context)params;
		}else{
			context=engine.createContext(params);
		}
		context.registFunctions(functionMap.values());
		String result=parser.parse(templateTxt, new BaseMacroResolver(context){
			@Override
			public String resolve(String macroName) {
				Object value=engine.evaluate((Context)this.param, macroName);
				if(value==null){
					return null;
				}
				return toString(value);
			}
		});
		return result;
	}
	
	

	@Override
	public void registFunction(Function f) {
		functionMap.put(f.getName(), f);
	}

	@Override
	public void registFunctions(Collection<Function> fs) {
		for(Function f:fs){
			registFunction(f);
		}
	}

}
