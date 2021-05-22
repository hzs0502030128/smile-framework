package org.smile.beans.converter.type;

import java.sql.Clob;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;

import org.smile.beans.converter.ConvertException;
import org.smile.beans.converter.AbstractTypeConverter;
import org.smile.math.NumberUtils;
import org.smile.reflect.Generic;
import org.smile.util.DateUtils;
import org.smile.util.StringUtils;

public class StringConverter extends AbstractTypeConverter<String> {
	
	private static final String DATE_NO_TIME="00:00:00";
	
	@Override
	public String convert(Generic generic, Object value) throws ConvertException {
		if (value == null || value instanceof String) {
			return (String) value;
		} else if (value instanceof Number) {
			return NumberUtils.format(value, "#.########");
		} else if (value instanceof Clob) {
			Clob clob = (Clob) value;
			try {
				int len = (int) clob.length();
				return clob.getSubString(1, len);
			} catch (SQLException e) {
				throw new ConvertException(" Clob convert to String error ", e);
			}
		} else if (value instanceof Collection) {
			return StringUtils.join((Collection) value, ",");
		} else if (value instanceof Object[]) {
			// 数组的情况下，把数组连接成一个字符串
			return StringUtils.join((Object[]) value, ',');
		} else if (value instanceof Date) {
			// 如果是日期类型 去掉时分秒为0的 只保留到天的格式
			String str=DateUtils.defaultFormat((Date)value);
			if(StringUtils.indexOf(str,DATE_NO_TIME,10)>0){
				return str.substring(0, 10);
			}
			return str;
		}
		return String.valueOf(value);
	}

	@Override
	public Class getType() {
		return String.class;
	}

}
