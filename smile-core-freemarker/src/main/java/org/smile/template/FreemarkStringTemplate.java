package org.smile.template;

import org.smile.template.handler.FreemarkTemplateHandler;
/**
 * 使用freemarker实现一个个模板
 * @author 胡真山
 *
 */
public class FreemarkStringTemplate extends StringTemplate {

	public FreemarkStringTemplate(String templateTxt) {
		super(templateTxt);
		this.handler=new FreemarkTemplateHandler(templateTxt);
	}

}
