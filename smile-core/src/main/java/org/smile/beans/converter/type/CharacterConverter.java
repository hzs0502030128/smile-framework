package org.smile.beans.converter.type;

import org.smile.beans.converter.ConvertException;
import org.smile.beans.converter.AbstractTypeConverter;
import org.smile.reflect.Generic;

public class CharacterConverter extends AbstractTypeConverter{
	
	@Override
	public Object convert(Generic generic, Object value) throws ConvertException {
		if(value==null||value instanceof Character){
			return value;
		}
		value=getFirst(value);
		return String.valueOf(value).charAt(0);
	}

	@Override
	public Class getType() {
		return Character.class;
	}

}
