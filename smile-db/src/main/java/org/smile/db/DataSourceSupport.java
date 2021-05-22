package org.smile.db;

import javax.sql.DataSource;

public interface DataSourceSupport {
	/**
	 * 设置 datasource 数据源
	 * @param dataSource dao中的 datasource
	 */
	public void setDataSource(DataSource dataSource);
	/**
	 * 获取数据源
	 * @return
	 */
	public DataSource getDataSource();
}
