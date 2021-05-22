package org.smile.beans.converter;

import org.smile.util.ObjectLenUtils;
/***
 * 具体类型转换接口
 * @author 胡真山
 * 2015年9月23日
 * @param <T>
 */
public abstract class AbstractTypeConverter<T> implements TypeConverter<T>{
	@Override
	public Class<T> getType(){
		throw new NullPointerException("not implements this method");
	}

	/***
	 * 如果value是有长度的数据类型 取第一个
	 * @param value
	 * @return
	 */
	protected Object getFirst(Object value){
		if(ObjectLenUtils.hasLength(value)){
			return ObjectLenUtils.get(value, 0);
		}
		return value;
	}
	/**
	 * 无泛型转换
	 * @param obj
	 * @return
	 * @throws ConvertException
	 */
	@Override
	public T  convert(Object obj) throws ConvertException{
		return convert(null, obj);
	}
}
