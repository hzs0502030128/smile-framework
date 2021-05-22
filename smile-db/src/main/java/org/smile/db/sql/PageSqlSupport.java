package org.smile.db.sql;

public interface PageSqlSupport {
	/**
	 * 总条数语句
	 * @return
	 */
	public  abstract String getCountSql(String sql);
	/**
	 * 数据库句
	 * @return
	 */
	public abstract  String getDataSql(String sql,int page,int size);
}
