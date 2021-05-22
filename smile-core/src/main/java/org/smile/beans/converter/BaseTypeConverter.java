package org.smile.beans.converter;

import org.smile.beans.converter.type.BooleanConverter;
import org.smile.beans.converter.type.ByteConverter;
import org.smile.beans.converter.type.CharacterConverter;
import org.smile.beans.converter.type.DoubleConverter;
import org.smile.beans.converter.type.FloatConverter;
import org.smile.beans.converter.type.IntegerConverter;
import org.smile.beans.converter.type.LongConverter;
import org.smile.beans.converter.type.ShortConverter;
import org.smile.reflect.ClassTypeUtils;
import org.smile.reflect.Generic;

/***
 * 基础数据类型转换器
 * @author 胡真山
 * 2015年9月23日
 */
public class BaseTypeConverter extends AbstractConverter {

	private static BaseTypeConverter instance;

	public static BaseTypeConverter getInstance() {
		if (instance == null) {
			instance = new BaseTypeConverter();
		}
		return instance;
	}

	/**注册默认转换*/
	protected void regsiterDefaultConverter() {
		typeConvertMap.put(byte.class, new ByteConverter());
		typeConvertMap.put(char.class, new CharacterConverter());
		typeConvertMap.put(double.class, new DoubleConverter());
		typeConvertMap.put(float.class, new FloatConverter());
		typeConvertMap.put(int.class, new IntegerConverter());
		typeConvertMap.put(long.class, new LongConverter());
		typeConvertMap.put(short.class, new ShortConverter());
		typeConvertMap.put(boolean.class, new BooleanConverter());
		typeConvertMap.put(Byte.class, new ByteConverter());
		typeConvertMap.put(Character.class, new CharacterConverter());
		typeConvertMap.put(Double.class, new DoubleConverter());
		typeConvertMap.put(Float.class, new FloatConverter());
		typeConvertMap.put(Integer.class, new IntegerConverter());
		typeConvertMap.put(Long.class, new LongConverter());
		typeConvertMap.put(Short.class, new ShortConverter());
		typeConvertMap.put(Boolean.class, new BooleanConverter());
	}

	@Override
	public <T> T convert(Class<T> type, Generic generic, Object value) throws ConvertException {
		T result = super.convert(type, generic, value);
		if (result == null) {
			return ClassTypeUtils.basicNullDefault(type);
		}
		return result;
	}

}
