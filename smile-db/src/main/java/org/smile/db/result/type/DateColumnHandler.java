package org.smile.db.result.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import org.smile.db.result.ColumnHandler;

public class DateColumnHandler implements ColumnHandler<Date> {

	@Override
	public Date getColumn(ResultSet rs, int index) throws SQLException {
		return rs.getTimestamp(index);
	}

	@Override
	public void setColumn(PreparedStatement ps, int index,Date value) throws SQLException {
		if(value instanceof Timestamp){
			ps.setTimestamp(index, (Timestamp)value);
		}
		ps.setTimestamp(index,new Timestamp(value.getTime()));
	}



	@Override
	public Date getColumn(ResultSet rs, String column) throws SQLException {
		return rs.getTimestamp(column);
	}

}
