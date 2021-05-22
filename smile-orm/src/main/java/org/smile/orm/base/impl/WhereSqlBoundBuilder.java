package org.smile.orm.base.impl;

import java.sql.SQLException;

import org.smile.db.sql.BoundSql;

public interface WhereSqlBoundBuilder {
	/**
	   *     对where语句进行解析
	 * 返回boundsql
	 * @param clazz
	 * @param whereSql
	 * @return
	 * @throws SQLException
	 */
	public BoundSql build(Class clazz,StringBuilder sql,String whereSql,Object[] params,Object[] newParams);
}
