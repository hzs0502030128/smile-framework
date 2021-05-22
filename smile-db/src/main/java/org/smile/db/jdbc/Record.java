package org.smile.db.jdbc;

import java.sql.SQLException;

/**
 * 数据库记录对应接口
 * @author 胡真山
 *
 */
public interface Record {
	/**
	 * 更新到数据库
	 */
	public void update();
	/**
	 * 更新数据库部分字段
	 * @param fileds
	 * @throws SQLException
	 */
	public void update(String[] fileds) ;
	/**
	 * 更新部分指定字段
	 * @param first
	 * @param strings
	 * @throws SQLException
	 */
	public void update(String first,String...strings ) ;
	/**
	 * 	需要更新批定字段
	 * @param fields 要更新的字段
	 * @param values 要更新的值
	 */
	public void update(String[] fields,Object[] values);
	/**
	 * 从数据库删除
	 */
	public void delete();
	/**
	 * @param where
	 * @throws SQLException
	 */
	public void delete(String where);
	/**
	 * 插入到数据库
	 */
	public void insert();
	/**
	 * 从数据库加载
	 */
	public void load();
	/**
	 * 从数据库加载
	 * @param fileds 需要加载的字段
	 * @throws SQLException
	 */
	public void load(String[] fields);
	/**
	 * 加载部分指定字段
	 * @param first
	 * @param strings
	 * @throws SQLException
	 */
	public void load(String first,String...strings );
	/**
	 * 保存插入或更新 以主键判断是否存在
	 * @throws SQLException
	 */
	public void save();
}

