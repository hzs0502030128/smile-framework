package org.smile.db.handler;

import java.sql.SQLException;

import org.smile.db.result.QueryResult;

public class ArrayRowHandler extends RowHandler {

	@Override
	public <E> E handle(QueryResult rs) throws SQLException {
		 int cols=rs.getColumnCount();
         Object[] result=new Object[cols];
         for (int i = 1; i <= cols; i++) {
            result[i-1]=rs.getObject(i);
         }
        return (E)result;
	}

	@Override
	public Class getResultClass() {
		return Object[].class;
	}

}
