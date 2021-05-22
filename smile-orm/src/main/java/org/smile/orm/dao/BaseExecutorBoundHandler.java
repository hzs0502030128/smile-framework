package org.smile.orm.dao;

import java.sql.SQLException;

import org.smile.db.sql.BoundSql;
import org.smile.orm.executor.MappedOperator;

public class BaseExecutorBoundHandler implements ExecutorBoundHandler{
	
	private Executor executor;
	
	@Override
	public BoundSql buildBoundSql(MappedOperator operator, Object param) throws SQLException {
		return operator.parseBoundSql(param);
	}

	@Override
	public Executor getExecutor() {
		return executor;
	}

	@Override
	public void setExecutor(Executor exe) {
		this.executor=exe;
	}

}
