package org.smile.db.result.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.smile.db.result.ColumnHandler;

public class DoubleColumnHandler implements ColumnHandler<Double> {

	@Override
	public Double getColumn(ResultSet rs, int index) throws SQLException {
		return rs.getDouble(index);
	}

	@Override
	public void setColumn(PreparedStatement ps, int index,Double value) throws SQLException {
		ps.setDouble(index, value);
	}

	@Override
	public Double getColumn(ResultSet rs, String column) throws SQLException {
		return rs.getDouble(column);
	}

}
