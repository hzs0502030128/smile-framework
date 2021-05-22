package org.smile.validate.handler;

import org.smile.beans.BeanProperties;
import org.smile.beans.converter.BeanException;
import org.smile.util.StringUtils;
import org.smile.util.ValidateUtils;
import org.smile.validate.Rule;
import org.smile.validate.ValidateSupport;
import org.smile.validate.ValidateText;

public class RegExpValidater extends Validater {
	
	@Override
	public boolean validate(Rule f, ValidateSupport action, BeanProperties properties) throws BeanException {
		Object value=getValue(f, action, properties);
		String valueStr=f.getRegexp().toString();
		boolean result;
		if(value==null){
			result=f.ifnull();
		}else{
			result=ValidateUtils.regexp((String)value,valueStr);
		}
		if(!result){
			error(action, f,StringUtils.replaceAll(valueStr,"\\$",""));
		}
		return result;
	}

	@Override
	public String defaultTxt() {
		return ValidateText.regexp;
	}

}
