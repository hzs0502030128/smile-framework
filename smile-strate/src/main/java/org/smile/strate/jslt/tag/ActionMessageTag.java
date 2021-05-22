package org.smile.strate.jslt.tag;

import java.io.IOException;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.smile.collection.CollectionUtils;
import org.smile.commons.StringBand;
import org.smile.strate.action.ActionSupport;
import org.smile.util.StringUtils;

public class ActionMessageTag extends TagSupport {
	private String name;
	@Override
	public int doStartTag() throws JspException {
		Map<String,String> errors=(Map)pageContext.getRequest().getAttribute(ActionSupport.ACTION_MESSAGE);
		if(CollectionUtils.notEmpty(errors)){
			String s=errors.get(name);
			if(StringUtils.notEmpty(s)){
				StringBand sb=new StringBand();
				sb.append("<div class='.actionMessage'>").append(s).append("</div>");
				sb.append("</div>");
				try {
					pageContext.getOut().print(sb);
				} catch (IOException e) {
					throw new JspException(e);
				}
			}
		}
		return SKIP_BODY;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
