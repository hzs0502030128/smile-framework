
package org.smile.tag.impl;

import java.util.Locale;

import org.smile.commons.ann.Attribute;
import org.smile.resource.DefaultResource;
import org.smile.tag.Scope;
import org.smile.tag.SimpleTag;

/**
 * 设置国际化文本
 */
public class SetTextTag extends SimpleTag {
	@Attribute
	protected String name;
	@Attribute
	protected String key;
	@Attribute
	protected Scope scope=Scope.page;

	

	@Override
	public void doTag() throws Exception {
		Locale local=tagContext.getLocale();
		String value=DefaultResource.getInstance().getString(local, key);
		tagContext.setAttribute(name, value, scope);
	}

}
