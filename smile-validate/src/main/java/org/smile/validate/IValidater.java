package org.smile.validate;

import org.smile.beans.BeanProperties;
import org.smile.beans.converter.BeanException;

public interface IValidater {
	/**验证一个配置*/
	public abstract boolean validate(Rule f, ValidateSupport action, BeanProperties properties) throws BeanException;
}
