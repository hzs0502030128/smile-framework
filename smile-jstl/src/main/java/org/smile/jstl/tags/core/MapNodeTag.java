
package org.smile.jstl.tags.core;

import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.smile.util.StringUtils;

/**
 * url tag 内部标签的父类
 * @author 胡真山
 *
 */
public class MapNodeTag extends TagSupport{
	
	private Object key;
	private Object value;
	
	@Override
	public int doStartTag() throws JspException {
		MapTag tag=(MapTag)pageContext.getAttribute(MapTag.MAP_TAG_FLAG);
		if(tag!=null){
			if(StringUtils.isNull(key)&&value instanceof Map){
				tag.getMapValues().putAll((Map)value);
			}else{
				tag.getMapValues().put(key,value);
			}
		}
		return SKIP_BODY;
	}

	public Object getKey() {
		return key;
	}

	public void setKey(Object key) {
		this.key = key;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
	
	
}