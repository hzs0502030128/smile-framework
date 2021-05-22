package org.smile.orm.executor;

import java.sql.SQLException;

import org.smile.db.sql.BoundSql;

public interface SqlBuilder {
	/**
	 * 创建一个sql绑定
	 * @param templateParam
	 * @param sqlParam 
	 * @return
	 * @throws SQLException
	 */
	public BoundSql buildSql(Object templateParam, Object[] sqlParam) throws SQLException;
	/**
	 * @param templateParam
	 * @param sqlParam
	 * @return
	 * @throws SQLException
	 */
	public BoundSql buildSql(Object templateParam, Object sqlParam) throws SQLException;
	/**
	 * 空参数的boundsql
	 * @return
	 */
	public BoundSql buildNullParameterBoundSql();
	
}
