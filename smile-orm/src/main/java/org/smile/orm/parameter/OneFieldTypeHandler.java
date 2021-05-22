package org.smile.orm.parameter;

import java.sql.SQLException;
import java.util.Map;

import org.smile.collection.CollectionUtils;
import org.smile.db.sql.BoundSql;
import org.smile.orm.executor.SqlBuilder;

public class OneFieldTypeHandler implements ParameterTypeHandler {
	@Override
	public BoundSql handleBoundSql(SqlBuilder builder,ParameterType parameterType, Object param) throws SQLException {
		Map<String,Object> map= CollectionUtils.hashMap(parameterType.getParamName(), param);
		return builder.buildSql(map,map);
	}

}
