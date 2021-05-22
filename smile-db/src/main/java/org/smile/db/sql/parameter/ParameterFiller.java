package org.smile.db.sql.parameter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.smile.log.LoggerHandler;

public interface ParameterFiller extends LoggerHandler{
	/**
	 * 把一个对象设置到ps中  
	 * @param ps
	 * @param value 一个对象
	 * @throws Exception
	 */
	public void fillObject(PreparedStatement ps,Object value) throws SQLException;
	/**
	 * 批量操作时设置
	 * @param ps
	 * @param value
	 * @throws SQLException
	 */
	public void fillBatchObject(PreparedStatement ps,Object value) throws SQLException;
}
