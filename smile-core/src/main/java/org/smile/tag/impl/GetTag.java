
package org.smile.tag.impl;

import org.smile.beans.BeanUtils;
import org.smile.commons.ann.Attribute;
import org.smile.tag.Scope;
import org.smile.tag.SimpleTag;
import org.smile.util.StringUtils;


public class GetTag extends SimpleTag{
	@Attribute
	protected String name;
	@Attribute
	protected String property;
	@Attribute
	protected Scope scope=Scope.context;

	@Override
	public void doTag() throws Exception {
		Object value=tagContext.getAttribute(name,scope);
		if(value!=null){
			if(StringUtils.isNull(property)){
				tagContext.getWriter().write(String.valueOf(value));
			}else{
				Object result=BeanUtils.getExpValue(value, property);
				if(result!=null){
					tagContext.getWriter().write(String.valueOf(value));
				}
			}
		}
	}
}
