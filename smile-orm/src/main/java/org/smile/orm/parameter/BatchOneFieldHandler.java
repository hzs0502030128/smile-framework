package org.smile.orm.parameter;

import java.sql.SQLException;
import java.util.Map;

import org.smile.collection.CollectionUtils;
import org.smile.db.sql.BoundSql;
import org.smile.orm.executor.SqlBuilder;
/***
 * 单值类型的处理批量
 * @author 胡真山
 *
 */
public class BatchOneFieldHandler implements ParameterTypeHandler{
	
	@Override
	public BoundSql handleBoundSql(SqlBuilder builder,ParameterType parameterType, Object param) throws SQLException {
		Map<String,Object> map= CollectionUtils.hashMap(parameterType.getParamName(), param);
		return builder.buildSql(map,param);
	}

}
