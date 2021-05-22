
package org.smile.tag.impl;

import org.smile.commons.ann.Attribute;
import org.smile.tag.Scope;
import org.smile.tag.SimpleTag;
import org.smile.util.StringUtils;

public class DeclareTag extends SimpleTag{
	@Attribute
	private Object value;
	@Attribute
	private String var;
	@Attribute
	private Scope scope=Scope.page;
	@Attribute
	private String type;
	
	@Override
	public void doTag() throws Exception {
		Object realValue=value;
		if(StringUtils.notEmpty(type)){
			realValue=TagUtils.convert(type, value);
		}
		tagContext.setAttribute(var, realValue, scope);
	}
}