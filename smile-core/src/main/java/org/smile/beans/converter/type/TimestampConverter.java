package org.smile.beans.converter.type;

import java.sql.Timestamp;
import java.util.Date;

import org.smile.beans.converter.AbstractTypeConverter;
import org.smile.beans.converter.ConvertException;
import org.smile.reflect.Generic;
import org.smile.util.DateUtils;


public class TimestampConverter extends AbstractTypeConverter<Timestamp>{

	@Override
	public Timestamp convert(Generic generic, Object value)
			throws ConvertException {
		if(value==null){
			return null;
		}
		value=getFirst(value);
		if(value instanceof Timestamp){
			return (Timestamp)value;
		}
		if(value instanceof Date){
			return new Timestamp(((Date)value).getTime());
		}
		return new Timestamp(DateUtils.parseDate(value.toString()).getTime());
	}

	@Override
	public Class<Timestamp> getType() {
		return Timestamp.class;
	}
	
}
