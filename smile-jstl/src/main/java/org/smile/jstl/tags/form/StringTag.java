package org.smile.jstl.tags.form;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.smile.beans.BeanUtils;
import org.smile.commons.Strings;
import org.smile.util.StringUtils;

public class StringTag extends SimpleTagSupport {
	private Object value;
	private String property;
	private String nullVal = Strings.BLANK;

	public void setValue(Object value) {
		this.value = value;
	}

	@Override
	public void doTag() throws JspException {
		JspWriter out = getJspContext().getOut();
		try {
			if (value == null) {
				out.print(nullVal);
			} else {
				if (StringUtils.notEmpty(property)) {
					value = BeanUtils.getExpValue(value, property);
				}
				if (value instanceof Iterable) {
					value = StringUtils.join((Iterable) value, ';');
				} else if (value instanceof Object[]) {
					value = StringUtils.join((Object[]) value, ';');
				}
				out.print(value);
			}
		} catch (Exception e) {
			throw new JspException(e);
		}
	}

	protected void reset() {
		value = null;
		property = null;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getNullVal() {
		return nullVal;
	}

	public void setNullVal(String nullVal) {
		this.nullVal = nullVal;
	}

	public Object getValue() {
		return value;
	}

}
