package org.smile.jstl.tags.sql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.jsp.tagext.TagSupport;
import javax.sql.DataSource;

import org.smile.db.DbManager;
import org.smile.db.sql.ArrayBoundSql;
import org.smile.db.sql.BoundSql;
import org.smile.db.sql.MappingBoundSql;
import org.smile.db.sql.NamedBoundSql;
import org.smile.http.Scopes;
import org.smile.spring.SpringBeanLocator;
import org.smile.util.StringUtils;
/**
 * 查询标签
 * @author strive
 *
 */
public abstract class QuerySupportTag extends TagSupport implements Scopes{
	/**
	 * 查询语句
	 */
	protected String sql;
	/**
	 * 参数
	 */
	protected Object params;
	/**
	 * 数据源名称
	 */
	protected String dataSourceName;
	
	protected Connection getConnection() throws SQLException{
		if(DbManager.hasDefaultDataSource()&&StringUtils.isEmpty(dataSourceName)){
			return DbManager.getConnection();
		}else if (SpringBeanLocator.isInit()){
		   if(StringUtils.isEmpty(dataSourceName)){
			   dataSourceName="dataSource";
		   }
		   DataSource datasource=SpringBeanLocator.getInstance().getBean(dataSourceName);
		   if(datasource!=null){
			   return datasource.getConnection();
		   }else{
			   return DbManager.getConnection(dataSourceName);
		   }
		}else if(StringUtils.isEmpty(dataSourceName)){
			throw new SQLException("dataSourceName must not null when not config a default dataSource");
		}
		return DbManager.getConnection(dataSourceName);
	}
	
	protected BoundSql getBoundSql() throws SQLException{
		if(params==null||params instanceof Object[]){
			return new ArrayBoundSql(sql, (Object[])params);
		}else if(params instanceof Map){
			return new NamedBoundSql(sql,(Map)params);
		}else{
			return new MappingBoundSql(sql,params);
		}
	}
	
	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	
	public Object getParams() {
		return params;
	}

	public void setParams(Object params) {
		this.params = params;
	}

	public String getDataSourceName() {
		return dataSourceName;
	}

	public void setDataSourceName(String dataSourceName) {
		this.dataSourceName = dataSourceName;
	}
}
