package org.smile.db.result.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.smile.db.result.ColumnHandler;

public class BooleanColumnHandler implements ColumnHandler<Boolean> {

	@Override
	public Boolean getColumn(ResultSet rs, int index) throws SQLException {
		return rs.getBoolean(index);
	}
	
	

	@Override
	public void setColumn(PreparedStatement ps, int index,Boolean value) throws SQLException {
		ps.setBoolean(index, value);
	}



	@Override
	public Boolean getColumn(ResultSet rs, String column) throws SQLException {
		return rs.getBoolean(column);
	}

}
