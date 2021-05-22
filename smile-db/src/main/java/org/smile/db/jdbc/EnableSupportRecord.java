package org.smile.db.jdbc;

import java.sql.SQLException;

import org.smile.db.jdbc.Record;

public interface EnableSupportRecord extends Record{
	/**
	 * 从数据库起用
	 */
	public void enabled() throws SQLException ;
	/**
	 * 从数据库失效
	 */
	public void disabled() throws SQLException ;
}
