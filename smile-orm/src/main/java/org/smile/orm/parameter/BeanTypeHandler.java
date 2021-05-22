package org.smile.orm.parameter;

import java.sql.SQLException;

import org.smile.db.sql.BoundSql;
import org.smile.orm.executor.SqlBuilder;

public class BeanTypeHandler implements ParameterTypeHandler {

	@Override
	public BoundSql handleBoundSql(SqlBuilder builder,ParameterType parameterType, Object param) throws SQLException {
		return builder.buildSql(param, param);
	}

}
