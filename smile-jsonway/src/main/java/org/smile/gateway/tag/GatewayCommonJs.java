package org.smile.gateway.tag;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.smile.commons.StringBand;

public class GatewayCommonJs extends TagSupport{
	
	protected static  String baseJs;
	
	protected String servletPath="gateway";

	@Override
	public int doStartTag() throws JspException {
		try {
			if(baseJs==null){
				StringBand js=new StringBand();
				String basePath=((HttpServletRequest)pageContext.getRequest()).getContextPath();
				js.append("<script type=\"text/javascript\" src=\"").append(basePath).append("/").append(servletPath).append("/resource/json.js\"></script>");
				js.append("<script type=\"text/javascript\" src=\"").append(basePath).append("/").append(servletPath).append("/resource/moo.js\"></script>");
				js.append("<script type=\"text/javascript\" src=\"").append(basePath).append("/").append(servletPath).append("/resource/jsonGateway.js\"></script>");
				baseJs=js.toString();
			}
			pageContext.getOut().println(baseJs);
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
	
}
