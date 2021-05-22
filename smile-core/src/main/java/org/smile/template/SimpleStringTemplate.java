package org.smile.template;

import org.smile.template.handler.SimpleTemplateHandler;

public class SimpleStringTemplate extends StringTemplate {
	
	public SimpleStringTemplate(String templateTxt){
		this(templateTxt,new SimpleTemplateHandler(templateTxt));
	}
	
	protected SimpleStringTemplate(String templateTxt,SimpleTemplateHandler handler){
		super(templateTxt);
		this.handler=handler;
	}
	
	public SimpleStringTemplate(TemplateParser parser,String templateTxt){
		super(templateTxt);
		this.handler=new SimpleTemplateHandler(parser, templateTxt);
	}
	
	public TemplateParser getParser(){
		return ((SimpleTemplateHandler)this.handler).getParser();
	}
	
	public boolean hasExpress(){
		StringTemplateParser parser=(StringTemplateParser)((SimpleTemplateHandler)this.handler).getParser();
		return parser.fragment(templateTxt).hasExpress();
	}
}
