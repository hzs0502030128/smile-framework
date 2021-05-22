package org.smile.validate.handler;

import org.smile.beans.BeanProperties;
import org.smile.beans.converter.BeanException;
import org.smile.util.ValidateUtils;
import org.smile.validate.Rule;
import org.smile.validate.ValidateSupport;
import org.smile.validate.ValidateText;


public class DateTimeValidater extends Validater {

	@Override
	public boolean validate(Rule f, ValidateSupport action, BeanProperties properties) throws BeanException {
		Object value=getValue(f, action, properties);
		if(value==null){
			return f.ifnull();
		}
		boolean result=ValidateUtils.datetime((String)value);
		if(!result){
			error(action, f);
		}
		return result;
	}

	@Override
	public String defaultTxt() {
		return ValidateText.datetime;
	}

}
