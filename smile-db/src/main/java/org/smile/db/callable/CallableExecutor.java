package org.smile.db.callable;

import java.sql.SQLException;

public interface CallableExecutor {
	/**
	 * 执行一个函数 指定返回值类型
	 * @param functionName
	 * @param returnType 返回值类型
	 * @param params
	 * @return
	 */
	public <T> T callFuction(String functionName,Class<T> returnType,Object... params) throws SQLException;
	/**
	 * 执行一个函数
	 * @param functionName
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public Object callFuction(String functionName,Object... params) throws SQLException;
}
