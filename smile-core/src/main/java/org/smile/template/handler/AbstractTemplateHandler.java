package org.smile.template.handler;

import java.io.StringWriter;
import java.io.Writer;

public abstract class AbstractTemplateHandler implements TemplateHandler{
	
	protected String templateTxt;
	
	public String processToString(Object params){
		Writer result=new StringWriter((int)(templateTxt.length()*1.5));
		parse(params, result);
		return result.toString();
	}

	public String getTemplateTxt() {
		return templateTxt;
	}
}
