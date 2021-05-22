package org.smile.db.pool.init;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.smile.db.config.ResourceConfig;

public abstract class DataSourceInitHandler {
	public abstract <T extends DataSource> T init(ResourceConfig config) throws SQLException;
}
