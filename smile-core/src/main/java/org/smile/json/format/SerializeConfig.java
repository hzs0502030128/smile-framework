package org.smile.json.format;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.Date;

/**
 * 序列化的设置信息
 * @author 胡真山
 *
 */
public interface SerializeConfig {
	/**空属性进行序列化*/
	public static final SerializeConfig NULL_VIEW=new DefaultSerializeConfig(true);
	/**空属性不进行序列化*/
	public static final SerializeConfig NULL_NOT_VIEW=new DefaultSerializeConfig();
	/***
	 * 空内容是否显示
	 * @return
	 */
	boolean isNullValueView();
	/**
	 * 格式化日期类型
	 * @param date
	 * @return
	 */
	String formatDate(Date date);
	/**
	 * 格式化sql日期类型
	 * @param date
	 * @return
	 */
	String formatSqlDate(java.sql.Date date);
	/**
	 * 忽略属性 不对此属性进行json序列化
	 * @param pd
	 * @return
	 */
	boolean ignore(PropertyDescriptor pd,Object value);
	/**
	 * 忽略 字段
	 * @param field
	 * @return
	 */
	boolean ignore(Field field,Object value);
}
