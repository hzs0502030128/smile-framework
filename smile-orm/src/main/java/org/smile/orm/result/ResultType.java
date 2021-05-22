package org.smile.orm.result;

import java.sql.SQLException;

import org.smile.db.handler.RowHandler;
import org.smile.db.sql.BoundSql;
import org.smile.db.sql.SQLRunner;

public interface ResultType {
	/**
	 * 反回类型的泛型 
	 * @return
	 */
	public Class getGenericType();
	/**
	 * 返回类型
	 * @return
	 */
	public Class getType();
	/**
	 * 是否是单对象返回 
	 * @return
	 */
	public boolean isSingleObj();
	/**
	 * 是否是单字段对象  如 String Integer Long
	 * @return
	 */
	public boolean isOneFieldType();
	/**
	 * 是否是布尔类型
	 * @return
	 */
	public boolean isBooleanType();
	/**
	 * 初始化后调用
	 */
	public void onInit();
	/**
	 * 创建数据行转换器
	 * @return
	 */
	public RowHandler createRowHandler();
	/**
	 *	 执行查询方法
	 * @param runner
	 * @param boundSql
	 * @return
	 * @throws SQLException
	 */
	public Object executeQuery(SQLRunner runner,BoundSql boundSql) throws SQLException;
}
