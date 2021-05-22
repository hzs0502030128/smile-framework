package org.smile.validate.handler;

import org.smile.beans.BeanProperties;
import org.smile.beans.converter.BeanException;
import org.smile.util.ValidateUtils;
import org.smile.validate.Rule;
import org.smile.validate.ValidateSupport;
import org.smile.validate.ValidateText;

public class EqualToValidater extends Validater {

	@Override
	public boolean validate(Rule f, ValidateSupport action, BeanProperties properties) throws BeanException {
		Object value=getValue(f, action, properties);
		boolean result;
		if(value==null){
			result= f.ifnull();
		}else{
			result=ValidateUtils.equalTo(f.getValue(), value);
		}
		if(!result){
			error(action, f, f.getValue());
		}
		return result;
	}

	@Override
	public String defaultTxt() {
		return ValidateText.eqaulTo;
	}

}
