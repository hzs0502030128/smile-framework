package org.smile.orm.executor;

import java.sql.SQLException;

import org.smile.beans.converter.BeanException;
import org.smile.orm.dao.Executor;

public class SelectMethod implements ExecuteMethod{

	@Override
	public Object execute(Executor executor, MappedOperator operator,Object param) throws SQLException, BeanException {
		return executor.query(operator, param);
	}
	
}
