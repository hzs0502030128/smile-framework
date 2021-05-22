package org.smile.orm.mapping.adapter;

public interface OrmMapperAdapter {
	/***
	 * 由对象名查找数据库表名
	 * @param objName
	 * @return
	 */
	public String getTableName(String objName);
	/**
	 * 由对象名与属性名查找数据库列名
	 * @param objName
	 * @param propertyName
	 * @return
	 */
	public String getColumnName(String objName,String propertyName);
}
