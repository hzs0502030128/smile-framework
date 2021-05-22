package org.smile.beans.converter.type;

import org.smile.beans.converter.ConvertException;
import org.smile.beans.converter.AbstractTypeConverter;
import org.smile.reflect.Generic;

public class ClassConverter extends AbstractTypeConverter<Class> {

	@Override
	public Class convert(Generic generic, Object value) throws ConvertException {
		if(value==null||value instanceof Class){
			return (Class)value;
		}else{
			String clazz;
			if(value instanceof String ){
				clazz=(String)value;
			}else{
				clazz=String.valueOf(getFirst(value));
			}
			try {
				return Class.forName(clazz);
			} catch (ClassNotFoundException e) {
				throw new ConvertException(value+"转换成class 失败", e);
			}
		}
	}

	@Override
	public Class getType() {
		return Class.class;
	}
	
	
}
