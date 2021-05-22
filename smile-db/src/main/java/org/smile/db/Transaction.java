package org.smile.db;

import java.sql.Connection;
import java.sql.SQLException;

import org.smile.log.LoggerHandler;
/***
 * 事务管理
 * @author 胡真山
 *
 */
public interface Transaction extends Closeable,LoggerHandler{
	/**
	 * 获取事物的连接
	 * @return
	 * @throws SQLException
	 */
	public abstract Connection getConnection()  throws SQLException ;
	/**
	 * 开始事务
	 * @throws SQLException
	 */
	public abstract void begin()  throws SQLException ;
	/**
	 * 提交事务
	 * @throws SQLException
	 */
	public abstract void commit()  throws SQLException ;
	/**
	 * 关闭事务
	 * @throws SQLException
	 */
	@Override
	public abstract void close()  throws SQLException ;
	/**
	 * 事务回滚
	 * @throws SQLException
	 */
	public abstract void rollback() throws SQLException;
}
