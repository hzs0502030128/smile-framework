package org.smile.strate.jslt.tag;

import java.io.IOException;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.smile.collection.CollectionUtils;
import org.smile.commons.StringBand;
import org.smile.strate.action.ActionSupport;

public class ActionErrorsTag extends TagSupport {

	@Override
	public int doStartTag() throws JspException {
		Map<String,String> errors=(Map)pageContext.getRequest().getAttribute(ActionSupport.ACTION_ERROR);
		if(CollectionUtils.notEmpty(errors)){
			StringBand sb=new StringBand();
			sb.append("<div class='.actionErrors'>");
			for(String s:errors.values()){
				sb.append("<div class='.actionError'>").append(s).append("</div>");
			}
			sb.append("</div>");
			try {
				pageContext.getOut().print(sb);
			} catch (IOException e) {
				throw new JspException(e);
			}
		}
		return SKIP_BODY;
	}
	
}
