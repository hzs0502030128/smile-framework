package org.smile.db.sql;

import org.smile.log.LoggerHandler;

/**
 * 属性与列映射的一个接口
 * @author 胡真山
 * @Date 2016年1月8日
 */
public interface Mapping extends LoggerHandler{
	/**
	 * java 属性名
	 * @return
	 */
	public String getPropertyName();
	/**
	 * 数据库字段名
	 * @return
	 */
	public String getColumnName();
	/**
	 * 用于属性名占位的表达式 
	 * 如：%{name}  :name  ? #{name}  几种常用的方式
	 * @return
	 */
	public String getPropertyExp();
}
