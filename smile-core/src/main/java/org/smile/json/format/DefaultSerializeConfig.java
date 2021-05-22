package org.smile.json.format;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.Date;

import org.smile.util.DateUtils;

/**不进行缩进处理*/
public class DefaultSerializeConfig implements SerializeConfig{
	/**
	 * 是否空属性可见
	 */
	protected boolean nullValueView=false;
	
	protected DefaultSerializeConfig(){}
	/**
	 * 是否空属性可见
	 * @param nullValueView
	 */
	protected DefaultSerializeConfig(boolean nullValueView){
		this.nullValueView=nullValueView;
	}

	@Override
	public boolean isNullValueView() {
		return nullValueView;
	}
	
	@Override
	public String formatDate(Date date) {
		return DateUtils.defaultFormat(date);
	}
	
	@Override
	public String formatSqlDate(java.sql.Date date) {
		return DateUtils.formatOnlyDate(date);
	}
	
	@Override
	public boolean ignore(PropertyDescriptor pd,Object value) {
		return false;
	}
	@Override
	public boolean ignore(Field field,Object value) {
		return false;
	}
}
