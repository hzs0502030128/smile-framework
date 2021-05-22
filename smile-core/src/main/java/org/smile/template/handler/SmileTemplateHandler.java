package org.smile.template.handler;

import java.io.Writer;

import org.smile.collection.ArrayUtils;
import org.smile.commons.SmileRunException;
import org.smile.tag.SimpleContext;
import org.smile.tag.TagContext;
import org.smile.tag.TagEngine;
import org.smile.template.IncludesContext;

public class SmileTemplateHandler extends AbstractTemplateHandler{
	
	private TagEngine engine;
	
	public SmileTemplateHandler(String templateText){
		this.templateTxt=templateText;
		this.engine=TagEngine.DEFAULT;
	}
	
	public SmileTemplateHandler(TagEngine engine,String templateText){
		this.templateTxt=templateText;
		this.engine=engine;
	}

	@Override
	public void parse(Object params, Writer out) {
		TagContext tagContext=convert(params);
		Writer oldWriter=tagContext.getWriter();
		try {
			tagContext.setWriter(out);
			this.engine.evaluate(tagContext,templateTxt);
			out.flush();
		} catch (Exception e) {
			throw new SmileRunException(e);
		}finally{
			tagContext.setWriter(oldWriter);
		}
	}
	
	protected TagContext convert(Object params){
		TagContext tagContext;
		if(params instanceof TagContext){
			tagContext=(TagContext)params;
		}else{
			tagContext=engine.createTagContext(params);
		}
		return tagContext;
	}

	@Override
	public void setIncludeContext(IncludesContext context) {
		String[] includeTxts=context.findInclude(templateTxt);
		if(ArrayUtils.notEmpty(includeTxts)){
			this.templateTxt=context.replaceInclude(includeTxts, templateTxt);
		}
	}

	public void setEngine(TagEngine engine) {
		this.engine = engine;
	}
	
	

}
