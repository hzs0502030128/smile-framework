package org.smile.validate.handler;

import org.smile.beans.BeanProperties;
import org.smile.beans.converter.BasicConverter;
import org.smile.beans.converter.BeanException;
import org.smile.beans.converter.Converter;
import org.smile.util.ValidateUtils;
import org.smile.validate.Rule;
import org.smile.validate.ValidateSupport;
import org.smile.validate.ValidateText;

public class LengthRangeValidater extends Validater {
	protected Converter convertor=BasicConverter.getInstance();
	@Override
	public boolean validate(Rule f, ValidateSupport action, BeanProperties properties) throws BeanException {
		Object value=getValue(f, action, properties);
		Object[] range=f.getRange();
		Integer min=convertor.convert(Integer.class, range[0]);
		Integer max=convertor.convert(Integer.class, range[1]);
		boolean result;
		if(value==null){
			result= f.ifnull();
		}else{
			result= ValidateUtils.lengthRange((String)value, min, max);
		}
		if(!result){
			error(action, f,min,max);
		}
		return result;
	}

	@Override
	public String defaultTxt() {
		return ValidateText.lengthRange;
	}

}
