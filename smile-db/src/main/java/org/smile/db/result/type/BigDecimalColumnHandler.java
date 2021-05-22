package org.smile.db.result.type;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.smile.db.result.ColumnHandler;

public class BigDecimalColumnHandler implements ColumnHandler<BigDecimal> {

	@Override
	public BigDecimal getColumn(ResultSet rs, int index) throws SQLException {
		return rs.getBigDecimal(index);
	}

	@Override
	public void setColumn(PreparedStatement ps, int index,BigDecimal value) throws SQLException {
		ps.setBigDecimal(index, value);
	}

	@Override
	public BigDecimal getColumn(ResultSet rs, String column) throws SQLException {
		return rs.getBigDecimal(column);
	}

}
