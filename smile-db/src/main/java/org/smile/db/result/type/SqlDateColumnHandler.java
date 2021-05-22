package org.smile.db.result.type;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.smile.db.result.ColumnHandler;

public class SqlDateColumnHandler implements ColumnHandler<Date> {

	@Override
	public Date getColumn(ResultSet rs, int index) throws SQLException {
		return rs.getDate(index);
	}

	@Override
	public void setColumn(PreparedStatement ps, int index,Date value) throws SQLException {
		ps.setDate(index, value);
	}

	@Override
	public Date getColumn(ResultSet rs, String column) throws SQLException {
		return rs.getDate(column);
	}

}
