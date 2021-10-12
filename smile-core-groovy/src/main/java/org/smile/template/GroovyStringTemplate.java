package org.smile.template;

import org.smile.template.handler.GroovyTemplateHandler;

public class GroovyStringTemplate extends StringTemplate {

	public GroovyStringTemplate(String templateTxt) {
		super(templateTxt);
		this.handler=new GroovyTemplateHandler(templateTxt);
	}

}
