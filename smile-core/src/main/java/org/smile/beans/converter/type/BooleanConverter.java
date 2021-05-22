package org.smile.beans.converter.type;

import org.smile.beans.converter.ConvertException;
import org.smile.beans.converter.AbstractTypeConverter;
import org.smile.collection.NoCaseStringSet;
import org.smile.reflect.Generic;
import org.smile.util.ObjectLenUtils;
import org.smile.util.StringUtils;


public class BooleanConverter extends AbstractTypeConverter<Boolean>{
	
	private static BooleanConverter instance=new BooleanConverter();
	
	public static BooleanConverter getInstance(){
		return instance;
	}
	/**
	 * 可转换成true的字符串
	 */
	static NoCaseStringSet trueValues=new NoCaseStringSet("y","yes","true","1");
	/**
	 * 可转换成false的字符串
	 */
	static NoCaseStringSet falseValues=new NoCaseStringSet("n","no","false","0");
	@Override
 	public Boolean convert(Generic generic, Object value) throws ConvertException {
		if(value==null||value instanceof Boolean){
			return (Boolean)value;
		}else if(StringUtils.isNull(value)){
			return null;
		}else{
			if(ObjectLenUtils.hasLength(value)){
				value=ObjectLenUtils.get(value, 0);
			}
			String str=String.valueOf(value);
			if(trueValues.contains(str)){
				return true;
			}else if(falseValues.contains(str)){
				return false;
			}else if(str.length()==0){
				return false;
			}
			throw new ConvertException("can convert value:"+value+" to boolean type");
		}
	}

	@Override
	public Class getType() {
		return Boolean.class;
	}
}
