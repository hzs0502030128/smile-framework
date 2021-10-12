
package org.smile.jstl.tags.core;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.JspTag;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * Else nested tag for {@link org.smile.jstl.tags.core.WhenTag}
 */
public class OtherTag extends TagSupport {
	@Override
    public int doStartTag() throws JspException {
    	JspTag parent = getParent();
        if(parent==null){
            throw new JspTagException("other tag must inside if tag or when tag");
        }
        if(parent instanceof WhenTag){
        	WhenTag elseTag=(WhenTag)parent;
        	if(elseTag.getTestValue()==false){
        		elseTag.clearBody();
        		elseTag.setUsedBody(true);
        		return EVAL_BODY_INCLUDE;
        	}
        	return SKIP_BODY;
        }else{
        	throw new JspTagException("other tag must inside if tag or when tag");
        }
    }
	
}