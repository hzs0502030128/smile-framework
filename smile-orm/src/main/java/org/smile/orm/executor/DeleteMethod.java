package org.smile.orm.executor;

import java.sql.SQLException;

import org.smile.orm.dao.Executor;

public class DeleteMethod implements ExecuteMethod{

	@Override
	public Object execute(Executor executor, MappedOperator operator,Object param) throws SQLException {
		return executor.delete(operator, param);
	}
}
