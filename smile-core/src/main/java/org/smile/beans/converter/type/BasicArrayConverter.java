package org.smile.beans.converter.type;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.List;

import org.smile.beans.converter.AbstractTypeConverter;
import org.smile.beans.converter.BasicConverter;
import org.smile.beans.converter.ConvertException;
import org.smile.beans.converter.Converter;
import org.smile.reflect.Generic;
/**
 * 通用的数据转换类 
 * 其实可以转换所有的数组 
 * @author 胡真山
 * 2015年9月23日
 */
public class BasicArrayConverter extends AbstractTypeConverter{
	
	protected Class clazz;
	
	protected Object model;
	
	public BasicArrayConverter(Class clazz){
		this.clazz=clazz;
		this.model=Array.newInstance(clazz, 0);
	}
	
	@Override
	public Class getType() {
		return model.getClass();
	}

	@Override
	public Object convert(Generic generic, Object value) throws ConvertException {
		if(value ==null || value.getClass()==getType()){
			return value;
		}
		if(value instanceof Object[]){
			Object[] resource=(Object[])value;
			Object result=Array.newInstance(clazz,resource.length);
			int i=0;
			for(Object obj:resource){
				Array.set(result,i++,BasicConverter.getInstance().convert(clazz, obj));
			}
			return result;
		}else if(value instanceof Collection){
			Collection resource=(Collection)value;
			Object result=Array.newInstance(clazz,resource.size());
			int i=0;
			for(Object obj:resource){
				Array.set(result,i++,BasicConverter.getInstance().convert(clazz, obj));
			}
			return result;
		}else {
			if(value instanceof String){
				List<String> ss=Converter.arraySplit.splitAndTrimNoBlack(value.toString());
				Object result=Array.newInstance(clazz,ss.size());
				int i=0;
				for(Object obj:ss){
					Array.set(result,i++,BasicConverter.getInstance().convert(clazz, obj));
				}
				return result;
			}else{
				Object result=Array.newInstance(clazz,1);
				Array.set(result,0,BasicConverter.getInstance().convert(clazz, value));
				return result;
			}
			
		}
	}
}
