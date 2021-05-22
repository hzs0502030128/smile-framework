package org.smile.strate.jslt.tag;

import java.util.Locale;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.smile.strate.i18n.I18nResource;
import org.smile.util.StringUtils;
/**
 * 国际化资源文件显示标签
 * @author 胡真山
 * @Date 2016年1月20日
 */
public class TextTag extends TagSupport {
	/**
	 * 用于缓存时变量名
	 * */
	private String var;
	/**
	 * 资源的键名称
	 */
	private String name;

	@Override
	public int doStartTag() throws JspException {
		Locale locale=pageContext.getRequest().getLocale();
		String text=I18nResource.message.getStringKeyDefault(locale, name);
		if(StringUtils.notEmpty(var)){
			pageContext.setAttribute(var, text);
		}
		return SKIP_BODY;
	}

	public void setVar(String var) {
		this.var = var;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
