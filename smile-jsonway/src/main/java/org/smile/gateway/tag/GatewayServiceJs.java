package org.smile.gateway.tag;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.smile.commons.StringBand;

public class GatewayServiceJs extends TagSupport{
	
	protected String servletPath="gateway";
	
	protected String serviceName;

	@Override
	public int doStartTag() throws JspException {
		try {
			StringBand js=new StringBand();
			String basePath=((HttpServletRequest)pageContext.getRequest()).getContextPath();
			js.append("<script type=\"text/javascript\" src=\"").append(basePath).append("/").append(servletPath).append("/").append(serviceName).append(".js\"></script>");
			pageContext.getOut().println(js.toString());
		} catch (IOException e) {
			throw new JspException(e);
		}
		return SKIP_BODY;
	}

	public String getServletPath() {
		return servletPath;
	}

	public void setServletPath(String servletPath) {
		this.servletPath = servletPath;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	
	
}
