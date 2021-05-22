package org.smile.db.result;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface  ColumnHandler<T> {
	/**
	 * 从数据库中取数据转为java类型
	 * @param rs
	 * @param index
	 * @return
	 * @throws SQLException 
	 */
	public abstract T getColumn(ResultSet rs,int index) throws SQLException;
	/**
	 * 从数据库中取数据转为java类型
	 * @param rs
	 * @param column
	 * @return
	 * @throws SQLException 
	 */
	public abstract T getColumn(ResultSet rs,String column) throws SQLException;
	/**
	 * 从java类型设置到数据库中
	 * @param ps
	 * @param index
	 * @throws SQLException 
	 */
	public abstract void setColumn(PreparedStatement ps,int index,T value) throws SQLException;
}
