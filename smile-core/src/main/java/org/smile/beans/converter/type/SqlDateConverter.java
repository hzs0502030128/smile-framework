package org.smile.beans.converter.type;

import java.util.Calendar;

import org.smile.beans.converter.AbstractTypeConverter;
import org.smile.beans.converter.ConvertException;
import org.smile.reflect.Generic;
import org.smile.util.DateUtils;



public class SqlDateConverter extends AbstractTypeConverter<java.sql.Date> {

	@Override
	public java.sql.Date convert(Generic generic, Object value) throws ConvertException {
		if (value == null) {
			return null;
		}
		value=getFirst(value);
		if (value instanceof java.sql.Date) {
			return (java.sql.Date) value;
		}
		if (value instanceof Calendar) {
			return new java.sql.Date(((Calendar)value).getTimeInMillis());
		}
		if (value instanceof java.util.Date) {
			return new java.sql.Date(((java.util.Date)value).getTime());
		}

		if (value instanceof Number) {
			return new java.sql.Date(((Number) value).longValue());
		}
		return new java.sql.Date(DateUtils.parseDate(value.toString().trim()).getTime());
	}

	@Override
	public Class<java.sql.Date> getType() {
		return java.sql.Date.class;
	}
	
}
