package org.smile.jstl.tags.form;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.smile.resource.DefaultResource;
/**
 * 国际化标签
 * @author 胡真山
 * 2015年12月21日
 */
public class TextTag extends TagSupport {
	
	public  static final String Locale_session="MESSAGE_RESOURCE_LOCALE_SESSION";
	
	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public int doStartTag() throws JspException {
		JspWriter out=pageContext.getOut();
		Locale locale=getLocale();
		try {
			out.print(DefaultResource.getInstance().getStringKeyDefault(locale, value));
		} catch (IOException e) {
			throw new JspException(e);
		}
		return SKIP_BODY;
	}
	
	private Locale getLocale(){
		HttpSession session=pageContext.getSession();
		Locale locale=(Locale)session.getAttribute(Locale_session);
		if(locale==null){
			locale=pageContext.getRequest().getLocale();
			session.setAttribute(Locale_session, locale);
		}
		return locale;
	}
	
}
