package org.smile.orm.parameter;

import java.sql.SQLException;
import java.util.Map;

import org.smile.db.sql.BoundSql;
import org.smile.db.sql.parameter.BatchParameterMap;
import org.smile.orm.executor.SqlBuilder;

public class CollectionTypeHandler implements ParameterTypeHandler {
	@Override
	public BoundSql handleBoundSql(SqlBuilder builder,ParameterType parameterType, Object param) throws SQLException {
		//有可能是batch操作
		Map<String,Object> map=new BatchParameterMap(param);
		map.put(parameterType.getParamName(), param);
		return builder.buildSql(map,map);
	}
}
