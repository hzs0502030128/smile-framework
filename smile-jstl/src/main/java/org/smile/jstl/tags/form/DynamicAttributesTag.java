package org.smile.jstl.tags.form;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.DynamicAttributes;
import javax.servlet.jsp.tagext.TagSupport;

import org.smile.collection.CollectionUtils;
import org.smile.commons.StringBand;
import org.smile.commons.Strings;

public class DynamicAttributesTag extends TagSupport implements DynamicAttributes{
	/**
	 * 动态属性
	 */
	protected Map<String,Object> dynAttrubite=new HashMap<String,Object>();
	
	@Override
	public void setDynamicAttribute(String uir, String name, Object value) throws JspException {
		this.dynAttrubite.put(name, value);
	}

	@Override
	public void release() {
		super.release();
		reset();
	}
	/**
	 * 重置参数
	 */
	protected void reset(){
		dynAttrubite.clear();
	}
	
	protected void addAttrubiteQuoteValue(StringBand html,Object value){
		html.append(Strings.QUOTE).append(value).append(Strings.QUOTE);
	}
	
	protected String getDynAttrubiteHtml(){
		if(CollectionUtils.notEmpty(dynAttrubite)){
			StringBand html=new StringBand();
			for(Map.Entry<String, Object> entry:dynAttrubite.entrySet()){
				html.append(" ").append(entry.getKey()).append("=");
				addAttrubiteQuoteValue(html, entry.getValue());
			}
			return html.toString();
		}
		return Strings.BLANK;
	}
}
