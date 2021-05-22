
package org.smile.tag.impl;

import org.smile.beans.BeanUtils;
import org.smile.commons.ann.Attribute;
import org.smile.tag.Scope;
import org.smile.tag.SimpleTag;
import org.smile.util.StringUtils;

public class SetTag extends SimpleTag {
	@Attribute
	protected String name;
	@Attribute
	protected String property;
	@Attribute
	protected Scope scope=Scope.page;
	@Attribute
	protected Object value;

	@Override
	public void doTag() throws Exception {
		if(!StringUtils.isNull(property)){
			value=BeanUtils.getExpValue(value, property);
		}
		tagContext.setAttribute(name, value, scope);
	}
	@Override
	protected void reset(){
		this.value=null;
		this.property=null;
	}
	
}
