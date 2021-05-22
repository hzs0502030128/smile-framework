package org.smile.db.result.type;

import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.smile.db.result.ColumnHandler;

public class ClobColumnHandler implements ColumnHandler<Clob>{

	@Override
	public Clob getColumn(ResultSet rs, int index) throws SQLException {
		return rs.getClob(index);
	}

	@Override
	public Clob getColumn(ResultSet rs, String column) throws SQLException {
		return rs.getClob(column);
	}

	@Override
	public void setColumn(PreparedStatement ps, int index, Clob value) throws SQLException {
		ps.setClob(index, value);
	}

}
