package org.smile.beans.converter.type;

import java.math.BigDecimal;

import org.smile.beans.converter.AbstractTypeConverter;
import org.smile.beans.converter.ConvertException;
import org.smile.reflect.Generic;
import org.smile.util.ObjectLenUtils;
import org.smile.util.StringUtils;

public abstract class NumberConverter<T> extends AbstractTypeConverter<T>{
	@Override
	public T convert(Generic generic, Object value) throws ConvertException {
		Number number=null;
		if(value==null){
			return null;
		}else if(value.getClass()==getType()){
			return (T)value;
		}else if(ObjectLenUtils.hasLength(value)){
			value=ObjectLenUtils.get(value, 0);
		}
		if(StringUtils.isNotNull(value)){
			if(value instanceof Number){
				number=(Number)value;
			}else if(value instanceof String){
				number= parseString((String)value);
			}else{
				number= parseString(String.valueOf(value));
			}
			return valueOf(number);
		}else{
			return null;
		}
	}
	
	protected abstract T valueOf(Number number);
	
	
	protected Number parseString(String value){
		return new BigDecimal(value);
	}

	@Override
	public Class getType() {
		return Number.class;
	}

}
