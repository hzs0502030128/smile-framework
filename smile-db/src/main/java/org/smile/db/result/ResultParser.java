package org.smile.db.result;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.smile.log.LoggerHandler;
/**
 *  数据库查询结果集解析成对象属性
 * @author 胡真山
 * @Date 2016年1月8日
 */
public interface ResultParser extends LoggerHandler{
	/**
	 * 结果集解析成对象属性
	 * @param rs 结果集
	 * @param target 用于封闭属性的目标对象
	 * @param column 属性列的封装信息
	 * @throws SQLException 
	 */
	public void parseResultSet(ResultSet rs, Object target, DatabaseColumn column) throws SQLException;

}
