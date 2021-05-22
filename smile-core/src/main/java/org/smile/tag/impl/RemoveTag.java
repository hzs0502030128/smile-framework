
package org.smile.tag.impl;

import org.smile.commons.ann.Attribute;
import org.smile.tag.Scope;
import org.smile.tag.SimpleTag;

public class RemoveTag extends SimpleTag{

	@Attribute
	protected String name;
	@Attribute
	protected Scope scope=Scope.page;


	@Override
	public void doTag() throws Exception {
		tagContext.removeAttribute(name,scope);
	}

}
