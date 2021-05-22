package org.smile.template.handler;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;

import org.smile.collection.ArrayUtils;
import org.smile.commons.SmileRunException;
import org.smile.template.IncludesContext;
import org.smile.template.StringTemplateParser;
import org.smile.template.StringTemplateParser.BaseMacroResolver;
import org.smile.template.TemplateParser;
import org.smile.template.TemplateParser.MacroResolver;

public class SimpleTemplateHandler extends AbstractTemplateHandler{

	protected TemplateParser parser;
	
	public SimpleTemplateHandler(String templateTxt){
		this.parser=new StringTemplateParser();
		this.templateTxt=templateTxt;
	}
	
	public SimpleTemplateHandler(TemplateParser parser,String templateTxt){
		this.parser=parser;
		this.templateTxt=templateTxt;
	}

	public TemplateParser getParser() {
		return parser;
	}

	public TemplateParser.Fragment fragment(){
		return this.parser.fragment(this.templateTxt);
	}


	public String processToString(Object params) {
		if(params==null){
			params=new HashMap();
		}
		MacroResolver resolver=new BaseMacroResolver(params);
		String result=parser.parse(templateTxt,resolver);
		return result;
	}
	
	@Override
	public void parse(Object params, Writer out) {
		try {
			out.write(processToString(params));
		} catch (IOException e) {
			throw new SmileRunException(e);
		}
	}

	@Override
	public void setIncludeContext(IncludesContext context) {
		String[] includeTxts=context.findInclude(templateTxt);
		if(ArrayUtils.notEmpty(includeTxts)){
			this.templateTxt=context.replaceInclude(includeTxts, templateTxt);
		}
	}
}
