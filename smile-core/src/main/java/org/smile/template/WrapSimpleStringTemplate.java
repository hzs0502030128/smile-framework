package org.smile.template;

import java.util.Collection;

import org.smile.function.Function;
import org.smile.template.handler.WrapSimpleTemplateHandler;

public class WrapSimpleStringTemplate extends SimpleStringTemplate implements WrapTemplate{
	
	public WrapSimpleStringTemplate(String templateTxt){
		super(templateTxt);
		this.handler=new WrapSimpleTemplateHandler(templateTxt);
	}
	
	public WrapSimpleStringTemplate(TemplateParser parser,String templateTxt){
		super(templateTxt);
		this.handler=new WrapSimpleTemplateHandler(parser, templateTxt);
	}
	
	@Override
	public void registFunction(Function f) {
		((WrapSimpleTemplateHandler)handler).registFunction(f);
	}

	@Override
	public void registFunctions(Collection<Function> fs) {
		for(Function f:fs){
			registFunction(f);
		}
	}
}
