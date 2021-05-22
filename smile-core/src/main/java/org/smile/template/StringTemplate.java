package org.smile.template;

import java.io.Writer;

import org.smile.template.adapter.TemplateAdapterContext;
import org.smile.template.handler.TemplateHandler;

/**
 * 以字符串做为一个模板
 * @author 胡真山
 *
 */
public class StringTemplate implements Template{
	
	protected TemplateHandler handler;
	
	protected String templateTxt;
	
	protected StringTemplate(String templateTxt){
		this.templateTxt=templateTxt;
	}
	
	public StringTemplate(String type,String templateText){
		this.templateTxt=templateText;
		this.handler=TemplateAdapterContext.getInstance().newTemplateHandler(type, templateText);
	}
	
	public StringTemplate(TemplateAdapterContext context,String type,String templateText){
		this.templateTxt=templateText;
		this.handler=context.newTemplateHandler(type, templateText);
	}
	
	public void setHandler(TemplateHandler handler){
		this.handler=handler;
	}
	
	@Override
	public void process(Object params, Writer out) {
		handler.parse(params, out);
	}

	@Override
	public String processToString(Object params) {
		return handler.processToString(params);
	}

	@Override
	public Object getTemplateSource() {
		return templateTxt;
	}

	public TemplateHandler getHandler() {
		return handler;
	}

}
