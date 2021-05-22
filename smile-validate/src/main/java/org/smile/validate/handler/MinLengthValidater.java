package org.smile.validate.handler;

import org.smile.beans.BeanProperties;
import org.smile.beans.converter.BasicConverter;
import org.smile.beans.converter.BeanException;
import org.smile.util.ValidateUtils;
import org.smile.validate.Rule;
import org.smile.validate.ValidateSupport;
import org.smile.validate.ValidateText;


public class MinLengthValidater extends Validater {
	@Override
	public boolean validate(Rule f, ValidateSupport action, BeanProperties properties) throws BeanException {
		Object value=getValue(f, action, properties);
		Object length=f.getValue();
		boolean result;
		if(value==null){
			result =f.ifnull();
		}else{
			Integer len=BasicConverter.getInstance().convert(Integer.class, length);
			result=ValidateUtils.minLength((String)value, len);
		}
		if(!result){
			error(action, f, length);
		}
		return result;
	}

	@Override
	public String defaultTxt() {
		return ValidateText.minLength;
	}

}
