package org.smile.db.result.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.smile.db.result.ColumnHandler;
import org.smile.util.StringUtils;

public class CharColumnHandler implements ColumnHandler<Character> {

	@Override
	public Character getColumn(ResultSet rs, int index) throws SQLException {
		String s=rs.getString(index);
		if(StringUtils.isEmpty(s)){
			return null;
		}else{
			return s.charAt(0);
		}
	}

	@Override
	public void setColumn(PreparedStatement ps, int index,Character value) throws SQLException {
		ps.setObject(index,value);
	}

	@Override
	public Character getColumn(ResultSet rs, String column) throws SQLException {
		String s=rs.getString(column);
		if(StringUtils.isEmpty(s)){
			return null;
		}else{
			return s.charAt(0);
		}
	}

}
