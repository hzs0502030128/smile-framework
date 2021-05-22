package org.smile.beans.converter;

import java.util.HashMap;
import java.util.Map;

import org.smile.reflect.Generic;

public abstract class AbstractConverter  implements Converter{
	
	protected  Map<Class,TypeConverter> typeConvertMap=new HashMap<Class,TypeConverter>();

	public void regsiterTypeConverter(TypeConverter<?> converter){
		this.typeConvertMap.put(converter.getType(), converter);
	}
	
	public AbstractConverter(){
		regsiterDefaultConverter();
	}
	
	/**注册默认转换*/
	protected abstract void regsiterDefaultConverter();
	/**
	 * 获取指定类型的转换类对象
	 * @param type
	 * @return
	 */
	public <T> TypeConverter<T> getTypeConverter(Class<T> type){
		return typeConvertMap.get(type);
	}
	
	@Override
	public <T> T convert(Class<T> type, Generic generic, Object value)
			throws ConvertException {
		TypeConverter<T> converter=getTypeConverter(type);
		if(converter!=null){
			if(value==null||converter.getType().isAssignableFrom(value.getClass())){
				return (T)value;
			}
			return converter.convert(generic, value);
		}
		throw new ConvertException("不支持的目标转换类型:"+type);
	}

	@Override
	public <T> T convert(Class<T> type, Object value) throws ConvertException {
		return convert(type,null, value);
	}
	
}
