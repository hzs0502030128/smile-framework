
package org.smile.jstl.tags.core;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import org.smile.web.CsrfShield;

public class CsrfUrlTag extends UrlTag{
	
	protected String csrfName;
	@Override
	public int doStartTag() throws JspException {
		super.doStartTag();
		if(csrfName==null){
			csrfName=CsrfShield.CSRF_TOKEN_NAME;
		}
		HttpSession session =pageContext.getSession();
		String value = CsrfShield.prepareCsrfToken(session);
		addParam(csrfName, value);
		return EVAL_BODY_INCLUDE;
	}
	public String getCsrfName() {
		return csrfName;
	}
	public void setCsrfName(String csrfName) {
		this.csrfName = csrfName;
	}
	
	
	
}