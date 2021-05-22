package org.smile.db.result.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.smile.db.result.ColumnHandler;

public class IntegerColumnHandler implements ColumnHandler<Integer> {

	@Override
	public Integer getColumn(ResultSet rs, int index) throws SQLException {
		return rs.getInt(index);
	}

	@Override
	public void setColumn(PreparedStatement ps, int index,Integer value) throws SQLException {
		ps.setInt(index, value);
	}

	@Override
	public Integer getColumn(ResultSet rs, String column) throws SQLException {
		return rs.getInt(column);
	}

}
