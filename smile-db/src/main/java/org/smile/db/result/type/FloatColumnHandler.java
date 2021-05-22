package org.smile.db.result.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.smile.db.result.ColumnHandler;

public class FloatColumnHandler implements ColumnHandler<Float> {

	@Override
	public Float getColumn(ResultSet rs, int index) throws SQLException {
		return rs.getFloat(index);
	}

	@Override
	public void setColumn(PreparedStatement ps, int index,Float value) throws SQLException {
		ps.setFloat(index, value);
	}

	@Override
	public Float getColumn(ResultSet rs, String column) throws SQLException {
		return rs.getFloat(column);
	}

}
