package org.smile.beans.converter.type;

import java.math.BigDecimal;

import org.smile.beans.converter.ConvertException;
import org.smile.reflect.Generic;
import org.smile.util.ObjectLenUtils;
import org.smile.util.StringUtils;

public class BigDecimalConverter extends NumberConverter<BigDecimal>{
	
	public static  BigDecimalConverter instance=new BigDecimalConverter();
	
	@Override
	public BigDecimal convert(Generic generic, Object value) throws ConvertException {
		if(StringUtils.isNotNull(value)){
			if(value instanceof BigDecimal){
				return (BigDecimal)value;
			}else if(value instanceof Number){
				return new BigDecimal(((Number)value).toString());
			}else if(value instanceof String){
				return new BigDecimal(((String)value).trim());
			}else {
				if(ObjectLenUtils.hasLength(value)){
					value=ObjectLenUtils.get(value, 0);
				}
				return new BigDecimal(String.valueOf(value));
			}
		}
		return null;
	}

	@Override
	public Class getType() {
		return BigDecimal.class;
	}

	@Override
	protected BigDecimal valueOf(Number number) {
		return new BigDecimal(number.doubleValue());
	}

}
