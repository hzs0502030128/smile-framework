package org.smile.beans.converter.type;

import java.math.BigInteger;

import org.smile.beans.converter.AbstractTypeConverter;
import org.smile.beans.converter.ConvertException;
import org.smile.reflect.Generic;

public class BigIntConverter extends AbstractTypeConverter<BigInteger> {

	@Override
	public BigInteger convert(Generic generic, Object value) throws ConvertException {
		if (value == null) {
			return null;
		}
		value=getFirst(value);
		if (value instanceof BigInteger) {
			return (BigInteger) value;
		}else if (value instanceof Number) {
			return new BigInteger(String.valueOf(((Number) value).longValue()));
		}
		try {
			return new BigInteger(value.toString().trim());
		} catch (NumberFormatException nfex) {
			throw new ConvertException(value+" cast to "+getType()+" error ",nfex);
		}
	}

	@Override
	public Class<BigInteger> getType() {
		return BigInteger.class;
	}

}
