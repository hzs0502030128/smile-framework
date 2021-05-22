package org.smile.beans.converter.type;

import java.math.BigDecimal;

import org.smile.beans.converter.ConvertException;
import org.smile.beans.converter.NumberTypeConverter;
import org.smile.beans.converter.TypeConverter;
import org.smile.collection.Interval;
import org.smile.reflect.Generic;


public class IntervalConverter implements TypeConverter<Interval>{

	@Override
	public Class<Interval> getType() {
		return Interval.class;
	}

	@Override
	public Interval convert(Generic generic, Object value) throws ConvertException {
		if(value instanceof String){
			String[] args=((String) value).split(",");
			if(args.length==2){
				if(generic==null){
					generic=new Generic(new Class[]{BigDecimal.class});
				}
				Class<Number> returnType=generic.values[0];
				Number start=NumberTypeConverter.getInstance().convert(returnType,args[0]);
				Number end=NumberTypeConverter.getInstance().convert(returnType,args[1]);
				return new Interval<Number>(start, end);
			}
			throw new ConvertException(value+"转换成"+getType()+"异常,不支持的格式");
		}
		return null;
	}

	@Override
	public Interval convert(Object value) throws ConvertException {
		return convert(null, value);
	}

}
