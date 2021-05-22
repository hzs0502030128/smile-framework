package org.smile.orm.parameter;

import java.sql.SQLException;

import org.smile.db.sql.BoundSql;
import org.smile.orm.executor.SqlBuilder;
/***
 * 空参数处理
 * @author 胡真山
 *
 */
public class NullTypeHandler implements ParameterTypeHandler {

	@Override
	public BoundSql handleBoundSql(SqlBuilder builder,ParameterType parameterType, Object param) throws SQLException {
		return builder.buildNullParameterBoundSql();
	}

}
