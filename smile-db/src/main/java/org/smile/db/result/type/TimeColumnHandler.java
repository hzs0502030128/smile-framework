package org.smile.db.result.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;

import org.smile.db.result.ColumnHandler;

public class TimeColumnHandler implements ColumnHandler<Time> {

	@Override
	public Time getColumn(ResultSet rs, int index) throws SQLException {
		return rs.getTime(index);
	}

	@Override
	public void setColumn(PreparedStatement ps, int index,Time value) throws SQLException {
		ps.setTime(index, value);
	}



	@Override
	public Time getColumn(ResultSet rs, String column) throws SQLException {
		return rs.getTime(column);
	}

}
