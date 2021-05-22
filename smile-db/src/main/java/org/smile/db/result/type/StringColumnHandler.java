package org.smile.db.result.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.smile.db.result.ColumnHandler;

public class StringColumnHandler implements ColumnHandler<String> {

	@Override
	public String getColumn(ResultSet rs, int index) throws SQLException {
		return rs.getString(index);
	}

	@Override
	public void setColumn(PreparedStatement ps, int index,String value) throws SQLException {
		ps.setString(index, value);
	}

	@Override
	public String getColumn(ResultSet rs, String column) throws SQLException {
		return rs.getString(column);
	}

}
