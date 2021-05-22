package org.smile.db;

import java.sql.SQLException;

public interface Closeable{
	/**
	 * 可关闭的方法
	 * @throws SQLException
	 */
	public void close() throws SQLException;
}
