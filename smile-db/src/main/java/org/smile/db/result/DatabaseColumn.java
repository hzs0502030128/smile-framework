package org.smile.db.result;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.smile.beans.converter.Converter;
import org.smile.commons.Column;
import org.smile.reflect.Generic;
/**
 * 用于和数据库列对应的属性封装
 * @author 胡真山
 * @Date 2016年1月8日
 * @param <T>
 */
public interface DatabaseColumn<T> extends Column<T>{
	/**
	 * 往目标对象中的属性列赋值
	 * @param target 目标对象
	 * @param value 需要赋的值
	 */
	public void writeValue(Object target, Object value);
	/**
	 * 获取一个属性列的值
	 * @param target 目标对象
	 * @return 属性的值
	 */
	public Object readValue(Object target);
	/**
	 * 数据库列名
	 * @return
	 */
	public String getColumnName();
	/**
	 * 是否是json存贮字段
	 * @return
	 */
	public boolean isJsonStore();
	
	/**
	 * 列属性的泛型
	 * @return
	 */
	public Generic getGeneric();
	/**
	 * 从结果集中解析出内容
	 * @param converter 用于转换的转换器
	 * @return
	 * @throws SQLException 
	 */
	public Object parseResultColumn(Converter converter,ResultSet rs) throws SQLException;
	/**
	 * 从结果集中解析到一个对象中
	 * @param rs 查询结果集
	 * @param target 用于封装的目标对象
	 * @throws SQLException 
	 */
	public void parseResultColumn(Converter converter,ResultSet rs, Object target) throws SQLException;
}
