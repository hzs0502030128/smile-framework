package org.smile.orm.parameter;

import java.sql.SQLException;
import java.util.Map;

import org.smile.db.sql.BoundSql;
import org.smile.db.sql.parameter.BatchParameterMap;
import org.smile.orm.executor.SqlBuilder;

public class BaseArrayTypeHandler implements ParameterTypeHandler{
	@Override
	public BoundSql handleBoundSql(SqlBuilder builder,ParameterType parameterType, Object args) throws SQLException {
		//有可能是batch操作
		Map<String,Object> map=new BatchParameterMap(((Object[])args)[0]);
		map.put(parameterType.getParamName(), args);
		return builder.buildSql(map,(Object[])args);
	}

}
