package org.smile.template.adapter;

import org.smile.template.Template;
import org.smile.template.handler.FreemarkTemplateHandler;
import org.smile.template.handler.TemplateHandler;

public class FreemarkerTemplateAdapter implements TemplateAdapter{
	
	static{
		//注册freemarker模板适配器
		TemplateAdapterContext.getInstance().registAdapter(Template.FREE_MARKER, new FreemarkerTemplateAdapter());
	}

	@Override
	public TemplateHandler newInstance(String templateText) {
		return new FreemarkTemplateHandler(templateText);
	}
	
}
