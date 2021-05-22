package org.smile.db.result.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.smile.db.result.ColumnHandler;

public class LongColumnHandler implements ColumnHandler<Long> {

	@Override
	public Long getColumn(ResultSet rs, int index) throws SQLException {
		return rs.getLong(index);
	}

	@Override
	public void setColumn(PreparedStatement ps, int index,Long value) throws SQLException {
		ps.setLong(index, value);
	}

	@Override
	public Long getColumn(ResultSet rs, String column) throws SQLException {
		return rs.getLong(column);
	}

}
