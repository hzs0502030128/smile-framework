package org.smile.db;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.Before;
import org.smile.db.config.ResourceConfig;
import org.smile.db.jdbc.JdbcRecordConfig;
import org.smile.db.pool.BasicDataSource;

public class BaseTest {
	
	protected DataSource ds;
	
	protected JdbcTemplate template;
	
	@Before
	public void before() throws SQLException{
		ResourceConfig config=new ResourceConfig();
		config.setUrl("jdbc:mysql://localhost:3306/mytest");
		config.setUsername("root");
		config.setPassword("password");
		config.setDriver("com.mysql.jdbc.Driver");
		ds=new BasicDataSource(config);
		template=new JdbcTemplate(ds);
		template.setTransactionHandler(new TransactionHandler());
		JdbcRecordConfig.getInstance().setJdbcTemplate(template);
	}
	
}
