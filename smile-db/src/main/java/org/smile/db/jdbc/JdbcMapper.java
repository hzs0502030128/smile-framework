package org.smile.db.jdbc;


public interface JdbcMapper<T> {
	/**
	 * 用于封装的类型
	 * @return
	 */
	public Class<T> mapperClass();
	/***
	 * 表信息配置
	 * @return
	 */
	public TableInfoCfg  tableConfig();
}
