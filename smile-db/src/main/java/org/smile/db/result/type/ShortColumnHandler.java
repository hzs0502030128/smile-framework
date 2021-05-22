package org.smile.db.result.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.smile.db.result.ColumnHandler;

public class ShortColumnHandler implements ColumnHandler<Short> {

	@Override
	public Short getColumn(ResultSet rs, int index) throws SQLException {
		return rs.getShort(index);
	}

	@Override
	public void setColumn(PreparedStatement ps, int index,Short value) throws SQLException {
		ps.setShort(index, value);
	}

	@Override
	public Short getColumn(ResultSet rs, String column) throws SQLException {
		return rs.getShort(column);
	}

}
