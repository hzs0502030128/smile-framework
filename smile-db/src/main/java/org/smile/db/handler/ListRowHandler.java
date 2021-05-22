package org.smile.db.handler;

import java.sql.SQLException;
import java.util.List;

import org.smile.db.result.QueryResult;
/**
 * 把结果转为List
 * @author Administrator
 */
public class ListRowHandler extends RowHandler {
	
	public ListRowHandler(Class<? extends List> resultClass){
		this.resultClass=resultClass;
	}

	@Override
	public <E> E handle(QueryResult rs) throws SQLException {
		List result;
		try {
			result = (List)resultClass.newInstance();
		} catch (Exception e) {
			throw new SQLException("init result object error", e);
		}
		int cols=rs.getColumnCount();
        for (int i = 1; i <= cols; i++) {
            result.add(rs.getObject(i));
        }
        return (E)result;
	}
}
