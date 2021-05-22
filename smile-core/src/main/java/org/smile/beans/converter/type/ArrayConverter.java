package org.smile.beans.converter.type;

import java.util.Collection;

import org.smile.beans.converter.BasicConverter;
import org.smile.beans.converter.ConvertException;
import org.smile.beans.converter.AbstractTypeConverter;
import org.smile.reflect.Generic;
/***
 * 非通用数组转的实现 
 * 
 * 是所有具体实现的父类
 * @author 胡真山
 * 2015年9月23日
 * @param <T>
 */
public abstract class ArrayConverter<T> extends AbstractTypeConverter<T[]> {
	
	protected Class<T> clazz;
	
	@Override
	public T[] convert(Generic generic, Object value) throws ConvertException {
		if(value ==null || value.getClass()==getType()){
			return (T[])value;
		}
		if(value instanceof Object[]){
			Object[] resource=(Object[])value;
			T[] result=defaultValue(resource.length);
			int i=0;
			for(Object obj:resource){
				result[i++]=BasicConverter.getInstance().convert(getOneType(),obj);
			}
			return result;
		}else if(value instanceof Collection){
			Collection resource=(Collection)value;
			T[] result=defaultValue(resource.size());
			int i=0;
			for(Object obj:resource){
				result[i++]=BasicConverter.getInstance().convert(getOneType(),obj);
			}
			return result;
		}else {
			T[] result=defaultValue(1);
			result[0]=BasicConverter.getInstance().convert(getOneType(),value);
			return result;
		}
	}
	
	protected abstract T[] defaultValue(int len);
	
	protected abstract Class<T> getOneType();

	@Override
	public abstract Class<T[]> getType() ;
	
}
