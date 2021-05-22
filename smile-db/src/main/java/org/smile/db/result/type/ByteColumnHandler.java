package org.smile.db.result.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.smile.db.result.ColumnHandler;

public class ByteColumnHandler implements ColumnHandler<Byte> {

	@Override
	public Byte getColumn(ResultSet rs, int index) throws SQLException {
		return rs.getByte(index);
	}

	@Override
	public void setColumn(PreparedStatement ps, int index,Byte value) throws SQLException {
		ps.setByte(index, value);
	}

	@Override
	public Byte getColumn(ResultSet rs, String column) throws SQLException {
		return rs.getByte(column);
	}

}
