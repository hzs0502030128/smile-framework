package org.smile.db.result;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ColumnParser {
	/**
	 * 把一个列的值转成封闭的对象
	 * @param rs
	 * @param column
	 * @param resultType
	 * @return
	 * @throws SQLException
	 */
	public Object parseResultColumn(ResultSet rs,DatabaseColumn column) throws SQLException;

	/**
	 * 是否以json格式存贮
	 * @param column
	 * @return
	 */
	public boolean jsonStore(Class fieldType);

	/**
	 * 从数据库结果集中获取一个字段解析成对象
	 * @param rs 数据结果集
	 * @param column 列名
	 * @param fieldType 要转换的目录类型
	 * @return
	 * @throws SQLException
	 */
	public Object parseResultColumn(ResultSet rs, String column, Class fieldType) throws SQLException;

}
