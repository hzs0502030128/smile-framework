package org.smile.db.jdbc;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public interface EnableRecordDao<E> extends RecordDao<E>{
	/**
	 * 按对象禁用数据
	 * @param id
	 * @throws SQLException
	 */
	public void disable(E obj);
	/**
	 * 启用数据按对象
	 * @param id
	 * @throws SQLException
	 */
	public void enable(E obj);
	/**
	 * 启用全部
	 * @throws SQLException
	 */
	public void enableAll();
	/**
	 * 全不启用
	 * @throws SQLException
	 */
	public void disableAll();
	/**
	 * 按where条件不启用
	 * @param where
	 * @param params
	 * @throws SQLException
	 */
	public void disable(String where,Object ... params);
	/**
	 * 按where条件启用
	 * @param where
	 * @param params
	 * @throws SQLException
	 */
	public void enable(String where,Object ... params);
	/**
	 * 按id 不启用
	 * @param ids
	 * @throws SQLException
	 */
	public void disableByIds(Collection ids);
	/**
	 * 按id启用
	 * @param ids
	 * @throws SQLException
	 */
	public void enableByIds(Collection ids);
	/**
	 * 批量禁用
	 * @param list
	 * @throws SQLException
	 */
	public void disableBatch(List<E> list);
	/**
	 * 批量启用
	 * @param list
	 * @throws SQLException
	 */
	public void enableBatch(List<E> list);
}
