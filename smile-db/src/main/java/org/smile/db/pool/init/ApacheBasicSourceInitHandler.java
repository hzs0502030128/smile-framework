package org.smile.db.pool.init;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.smile.beans.BeanUtils;
import org.smile.db.config.ResourceConfig;

public  class ApacheBasicSourceInitHandler extends DataSourceInitHandler{

	@Override
	public <T extends DataSource> T init(ResourceConfig config) throws SQLException {
		try {
			Class<T> dataSourceClazz=(Class<T>)Class.forName(config.getType());
			T dataSource=dataSourceClazz.newInstance();
			BeanUtils.fillProperties(dataSource, config);
			return dataSource;
		} catch (Exception e) {
			throw new SQLException("init datasource "+config.getType()+" errro ", e);
		}
	}
	
}
