package org.smile.beans.converter.type;

import java.sql.Time;
import java.util.Date;

import org.smile.beans.converter.AbstractTypeConverter;
import org.smile.beans.converter.ConvertException;
import org.smile.reflect.Generic;
import org.smile.util.DateUtils;


public class TimeConverter extends AbstractTypeConverter<Time>{

	@Override
	public Time convert(Generic generic, Object value)
			throws ConvertException {
		if(value==null){
			return null;
		}
		value=getFirst(value);
		if(value instanceof Time){
			return (Time)value;
		}
		if(value instanceof Date){
			return new Time(((Date)value).getTime());
		}
		return new Time(DateUtils.parseDate(value.toString()).getTime());
	}

	@Override
	public Class<Time> getType() {
		return Time.class;
	}
	
}
