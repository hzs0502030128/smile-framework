package org.smile.validate.handler;

import org.smile.beans.BeanProperties;
import org.smile.beans.converter.BasicConverter;
import org.smile.beans.converter.BeanException;
import org.smile.beans.converter.Converter;
import org.smile.util.ValidateUtils;
import org.smile.validate.Rule;
import org.smile.validate.ValidateSupport;
import org.smile.validate.ValidateText;

public class RangeValidater extends Validater {
	protected Converter convertor=BasicConverter.getInstance();
	@Override
	public boolean validate(Rule f, ValidateSupport action, BeanProperties properties) throws BeanException {
		Object value=getValue(f, action, properties);
		Comparable[] range=f.getRange();
		Comparable min=range[0];
		Comparable max=range[1];
		//转成相应的类型
		boolean result;
		if(value==null){//为空时返回false
			result= f.ifnull();
		}else{
			min=(Comparable)convertor.convert(value.getClass(), min);
			max=(Comparable)convertor.convert(value.getClass(), max);
			result=ValidateUtils.range((Comparable)value, min, max);
		}
		if(!result){
			error(action, f, min,max);
		}
		return result;
	}

	@Override
	public String defaultTxt() {
		return ValidateText.range;
	}
}
