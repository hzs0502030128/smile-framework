package org.smile.db.result.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.smile.db.result.ColumnHandler;

public class ByteArrayColumnHandler implements ColumnHandler<byte[]> {

	@Override
	public byte[] getColumn(ResultSet rs, int index) throws SQLException {
		return rs.getBytes(index);
	}

	@Override
	public void setColumn(PreparedStatement ps, int index,byte[] value) throws SQLException {
		ps.setBytes(index, value);
	}

	@Override
	public byte[] getColumn(ResultSet rs, String column) throws SQLException {
		return rs.getBytes(column);
	}

}
