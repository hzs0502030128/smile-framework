package org.smile.orm.record;

import java.sql.SQLException;

import org.smile.db.jdbc.EnableSupportRecord;

public class EnableOrmRecord extends OrmRecord implements EnableSupportRecord{

	@Override
	public void enabled() throws SQLException {
		dao().enable(entity());
	}

	@Override
	public void disabled() throws SQLException {
		dao().disable(entity());
	}

}
