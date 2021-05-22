package org.smile.db.pool.init;

import java.sql.SQLException;

import org.smile.db.config.ResourceConfig;
import org.smile.db.pool.BasicDataSource;

public  class SmileSourceInitHandler extends DataSourceInitHandler{

	@Override
	public BasicDataSource init(ResourceConfig config) throws SQLException {
		return new BasicDataSource(config);
	}
}
