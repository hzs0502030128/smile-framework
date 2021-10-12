
package org.smile.jstl.tags.core;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.JspTag;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.smile.jstl.tags.TagUtils;

/**
 * Else nested tag for {@link org.smile.jstl.tags.core.ifelseTag}
 */
public class ElseTag extends SimpleTagSupport {
    @Override
    public void doTag() throws JspException {
    	 JspTag parent = getParent();
         boolean result=TagUtils.getIfElseSuccess(parent,(PageContext)getJspContext());
         if(!result){
        	 TagUtils.invokeBody(getJspBody());
         }
    }
}