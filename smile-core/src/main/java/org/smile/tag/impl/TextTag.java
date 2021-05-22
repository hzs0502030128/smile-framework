package org.smile.tag.impl;

import java.util.Locale;

import org.smile.commons.ann.Attribute;
import org.smile.resource.DefaultResource;
import org.smile.tag.SimpleTag;
/**
 * 国际化标签
 * @author 胡真山
 * 2015年12月21日
 */
public class TextTag extends SimpleTag {
	
	@Attribute
	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public void doTag() throws Exception {
		Locale locale=tagContext.getLocale();
		String text=DefaultResource.getInstance().getStringKeyDefault(locale, value);
		tagContext.getWriter().write(text);
	}
}
