package org.smile.report.function;

import org.smile.template.StringTemplate;
import org.smile.template.Template;
/**
 * 模板函数  函数里是一个基础模板
 * @author 胡真山
 *
 */
public class TemplateFunction extends AbstractFunction {
	/**模板函数单例使用*/
	protected static TemplateFunction instance=new TemplateFunction();
	
	@Override
	public Object convert(Object oneData, String exp, Object expValue) {
		Template template=new StringTemplate(Template.SMILE,exp);
		return template.processToString(oneData);
	}
	
	@Override
	public boolean needContext() {
		return true;
	}
}
