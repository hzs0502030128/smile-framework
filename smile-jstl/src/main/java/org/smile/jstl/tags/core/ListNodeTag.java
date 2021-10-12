
package org.smile.jstl.tags.core;

import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * url tag 内部标签的父类
 * @author 胡真山
 *
 */
public class ListNodeTag extends TagSupport{
	
	private Object value;
	
	private boolean isList=false;
	
	@Override
	public int doStartTag() throws JspException {
		ListTag tag=(ListTag)pageContext.getAttribute(ListTag.LIST_TAG_FLAG);
		if(tag!=null){
			if(isList&&value instanceof List){
				tag.getListValues().addAll((List)value);
			}else{
				tag.getListValues().add(value);
			}
		}
		return SKIP_BODY;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public boolean isList() {
		return isList;
	}
	public void setList(boolean isList) {
		this.isList = isList;
	}
	
}