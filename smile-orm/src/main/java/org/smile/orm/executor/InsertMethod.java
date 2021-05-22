package org.smile.orm.executor;

import java.sql.SQLException;

import org.smile.orm.dao.Executor;

public class InsertMethod implements ExecuteMethod{

	@Override
	public Object execute(Executor executor, MappedOperator operator,Object param) throws SQLException {
		return executor.insert(operator, param);
	}
}
