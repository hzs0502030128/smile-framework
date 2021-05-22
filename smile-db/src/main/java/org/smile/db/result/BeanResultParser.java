package org.smile.db.result;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BeanResultParser implements ResultParser{
	
	/**列解析器*/
	protected ColumnParser columnParser=ResultUtils.columnParser;
	
	@Override
	public void parseResultSet(ResultSet rs, Object target, DatabaseColumn column) throws SQLException {
		column.parseResultColumn(ResultUtils.CONVERTER, rs, target);
	}

	public void setColumnParser(ColumnParser columnParser) {
		this.columnParser = columnParser;
	}
}
