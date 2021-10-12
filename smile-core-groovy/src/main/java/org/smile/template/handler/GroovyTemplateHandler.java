package org.smile.template.handler;

import groovy.lang.Writable;
import groovy.text.SimpleTemplateEngine;
import groovy.text.Template;
import groovy.text.TemplateEngine;

import java.io.Writer;
import java.util.Map;

import org.smile.beans.BeanUtils;
import org.smile.beans.converter.BeanException;
import org.smile.collection.ArrayUtils;
import org.smile.commons.SmileRunException;
import org.smile.template.IncludesContext;
import org.smile.template.TemplateHandlerException;

public class GroovyTemplateHandler extends  AbstractTemplateHandler {
	
	private TemplateEngine engine=new SimpleTemplateEngine();;
	
	private static final String SINGLE_PARA_VAR="value";
	
	private Template template;
	
	public GroovyTemplateHandler(String templateText){
		this.templateTxt=templateText;
		try {
			template=engine.createTemplate(templateText);
		} catch (Exception e) {
			throw new TemplateHandlerException("process template error,content "+templateText, e);
		}
	}
	
	@Override
	public void parse(Object params, Writer out) {
		try {
			Writable r= template.make(convertToMakeMap(params));
			r.writeTo(out);
		} catch (Exception e) {
			throw new TemplateHandlerException("process template error,content "+templateTxt+" params "+params, e);
		}
	}
	/**
	 * 转换成用于填充模板的Map对象
	 * @param paramObj
	 * @return
	 */
	protected Map convertToMakeMap(Object paramObj) throws BeanException{
		Map realParam=null;
		if(paramObj instanceof Map){
			realParam=(Map)paramObj;
		}else if(paramObj!=null){
			realParam=BeanUtils.mapFromBean(paramObj);
			if(realParam.isEmpty()){
				realParam.put(SINGLE_PARA_VAR, paramObj);
			}
		}
		return realParam;
	}

	@Override
	public void setIncludeContext(IncludesContext context) {
		String[] includeTxts=context.findInclude(templateTxt);
		if(ArrayUtils.notEmpty(includeTxts)){
			try {
				this.template=engine.createTemplate(context.replaceInclude(includeTxts, templateTxt));
			}catch(TemplateHandlerException ee){
				throw ee;
			}catch (Exception e) {
				throw new SmileRunException(e);
			}
		}
	}
}
