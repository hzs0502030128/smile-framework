package org.smile.db.pool.init;

import java.sql.SQLException;

import org.smile.db.config.ResourceConfig;
import org.smile.db.pool.Pool2DataSource;

public  class Pool2DataSourceInitHandler extends DataSourceInitHandler{

	@Override
	public Pool2DataSource init(ResourceConfig config) throws SQLException {
		return new Pool2DataSource(config);
	}
}
