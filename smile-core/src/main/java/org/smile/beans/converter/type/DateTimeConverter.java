package org.smile.beans.converter.type;

import java.util.Date;

import org.smile.beans.converter.AbstractTypeConverter;
import org.smile.beans.converter.ConvertException;
import org.smile.datetime.DateTime;
import org.smile.reflect.Generic;
import org.smile.util.DateUtils;

public class DateTimeConverter extends AbstractTypeConverter<DateTime>{
	
	@Override
	public DateTime convert(Generic generic, Object value) throws ConvertException {
		if(value ==null ){
			return null;
		}
		if(value instanceof Date){
			return new DateTime((Date)value);
		}
		value=getFirst(value);
		Date date=DateUtils.convertToDate(value);
		return new DateTime(date);
	}

	@Override
	public Class<DateTime> getType() {
		return DateTime.class;
	}
}
