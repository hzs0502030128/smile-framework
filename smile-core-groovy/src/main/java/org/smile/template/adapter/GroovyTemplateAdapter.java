package org.smile.template.adapter;
import org.smile.template.Template;
import org.smile.template.handler.GroovyTemplateHandler;
import org.smile.template.handler.TemplateHandler;

public class GroovyTemplateAdapter implements TemplateAdapter{

	static{
		//注册groovy模板适配器
		TemplateAdapterContext.getInstance().registAdapter(Template.GROOVY, new GroovyTemplateAdapter());
	}
	
	@Override
	public TemplateHandler newInstance(String templateText) {
		return new GroovyTemplateHandler(templateText);
	}
	
}
