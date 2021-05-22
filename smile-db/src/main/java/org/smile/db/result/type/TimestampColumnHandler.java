package org.smile.db.result.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.smile.db.result.ColumnHandler;

public class TimestampColumnHandler implements ColumnHandler<Timestamp>{

	@Override
	public Timestamp getColumn(ResultSet rs, int index) throws SQLException {
		return rs.getTimestamp(index);
	}

	@Override
	public Timestamp getColumn(ResultSet rs, String column) throws SQLException {
		return rs.getTimestamp(column);
	}

	@Override
	public void setColumn(PreparedStatement ps, int index, Timestamp value) throws SQLException {
		ps.setTimestamp(index, value);
	}

}
