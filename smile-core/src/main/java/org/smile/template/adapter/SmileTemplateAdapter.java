package org.smile.template.adapter;

import org.smile.commons.SmileRunException;
import org.smile.template.handler.TemplateHandler;

public class SmileTemplateAdapter implements TemplateAdapter{
	/**实例化hanlder的类型*/
	private Class handlerClass;
	
	protected SmileTemplateAdapter(Class handlerClass){
		this.handlerClass=handlerClass;
	}
	@Override
	public TemplateHandler newInstance(String templateText) {
		try {
			return (TemplateHandler) handlerClass.getConstructor(String.class).newInstance(templateText);
		} catch (Exception e) {
			throw new SmileRunException(e);
		}
	}
	
}
