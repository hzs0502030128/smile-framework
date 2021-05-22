package org.smile.beans.converter;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.smile.beans.converter.type.BigDecimalConverter;
import org.smile.beans.converter.type.BigIntConverter;
import org.smile.beans.converter.type.ByteConverter;
import org.smile.beans.converter.type.DoubleConverter;
import org.smile.beans.converter.type.FloatConverter;
import org.smile.beans.converter.type.IntegerConverter;
import org.smile.beans.converter.type.LongConverter;
import org.smile.beans.converter.type.ShortConverter;

/***
 * 基础数据类型转换器
 * @author 胡真山
 * 2015年9月23日
 */
public class NumberTypeConverter extends AbstractConverter {

	private static NumberTypeConverter instance;

	public static NumberTypeConverter getInstance() {
		if (instance == null) {
			instance = new NumberTypeConverter();
		}
		return instance;
	}

	/**注册默认转换*/
	protected void regsiterDefaultConverter() {
		typeConvertMap.put(Byte.class, new ByteConverter());
		typeConvertMap.put(Double.class, new DoubleConverter());
		typeConvertMap.put(Float.class, new FloatConverter());
		typeConvertMap.put(Integer.class, new IntegerConverter());
		typeConvertMap.put(Long.class, new LongConverter());
		typeConvertMap.put(Short.class, new ShortConverter());
		typeConvertMap.put(BigDecimal.class, new BigDecimalConverter());
		typeConvertMap.put(BigInteger.class, new BigIntConverter());
	}
	
}
