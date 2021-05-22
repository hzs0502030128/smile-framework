package org.smile.db.result.type;

import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.smile.db.result.ColumnHandler;

public class BlobColumnHandler implements ColumnHandler<Blob>{

	@Override
	public Blob getColumn(ResultSet rs, int index) throws SQLException {
		return rs.getBlob(index);
	}

	@Override
	public Blob getColumn(ResultSet rs, String column) throws SQLException {
		return rs.getBlob(column);
	}

	@Override
	public void setColumn(PreparedStatement ps, int index, Blob value) throws SQLException {
		ps.setBlob(index, value);
	}

}
