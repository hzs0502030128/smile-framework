package org.smile.orm.load;

import java.util.List;

import org.smile.log.LoggerHandler;
import org.smile.orm.OrmApplication;

public interface Application extends LoggerHandler{
	/**
	 * 设置数据源
	 * @param dataSource
	 */
	public void setDataSource(Object dataSource);
	/**
	 * 设置扫描目录
	 * @param packageString
	 */
	public void setPackageString(List<String> packageString);
	/**
	 * 初始化应用程序
	 */
	public void initOrmApplication();
	/**
	 * 获取应用程序
	 * @return
	 */
	public OrmApplication getOrmApplication();
	/**
	 * 设置数据库方言
	 * @param dialect
	 */
	public void setDialect(String dialect);
}
