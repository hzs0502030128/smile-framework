package org.smile.template.handler;

import java.io.IOException;
import java.io.Writer;

import org.smile.template.IncludesContext;

public class NoneTemplateHandler extends AbstractTemplateHandler{
	
	public  NoneTemplateHandler(String templateTxt){
		this.templateTxt=templateTxt;
	}
	
	@Override
	public void parse(Object params, Writer out) {
		try {
			out.write(this.templateTxt);
		} catch (IOException e) {
		}
	}
	
	@Override
	public String processToString(Object params) {
		return this.templateTxt;
	}

	@Override
	public void setIncludeContext(IncludesContext context) {
		
	}

}
