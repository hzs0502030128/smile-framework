package org.smile.db.jdbc;

import org.smile.db.JdbcTemplate;

public class JdbcRecordConfig {
	
	static JdbcRecordConfig instance=new JdbcRecordConfig();
	
	private JdbcTemplate  jdbcTemplate;

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public static JdbcRecordConfig getInstance() {
		return instance;
	}
	
}
