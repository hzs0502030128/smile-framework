package org.smile.beans.converter.type;

import java.util.Date;

import org.smile.beans.converter.AbstractTypeConverter;
import org.smile.beans.converter.ConvertException;
import org.smile.reflect.Generic;
import org.smile.util.DateUtils;

public class DateConverter extends AbstractTypeConverter<Date>{
	
	@Override
	public Date convert(Generic generic, Object value) throws ConvertException {
		if(value ==null ){
			return null;
		}
		if(value instanceof Date){
			return (Date)value;
		}
		value=getFirst(value);
		return DateUtils.convertToDate(value);
	}

	@Override
	public Class<Date> getType() {
		return Date.class;
	}
}
