package org.smile.orm.executor;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;

import org.smile.beans.converter.BeanException;
import org.smile.collection.ArrayIterable;
import org.smile.collection.CollectionUtils;
import org.smile.db.sql.BoundSql;
import org.smile.db.sql.parameter.BatchParameterMap;
import org.smile.log.LoggerHandler;
import org.smile.orm.dao.Executor;
import org.smile.reflect.ClassTypeUtils;

public class BatchMethod implements ExecuteMethod,LoggerHandler{

	@Override
	public Object execute(Executor executor, MappedOperator operator,Object param) throws SQLException, BeanException {
		Collection batchList=getBatchValues(operator,param);
		if(CollectionUtils.isEmpty(batchList)){
			logger.warn(" the batch method list is empty");
			return 0;
		}
		Object firstParam=CollectionUtils.get(batchList, 0);
		if(firstParam==null){
			throw new SQLException("batch first param must not null ");
		}
		BoundSql boundSql=operator.parseBoundSql(firstParam);
		if(param instanceof Map){
			boundSql.setBatchMap((Map)param);
		}
		return executor.batch(operator,boundSql,batchList);
	}
	/**
	 *	 获取批量操作的参数集合
	 * @param operator
	 * @param param
	 * @return
	 * @throws SQLException
	 */
	protected Collection<?> getBatchValues(MappedOperator operator,Object param) throws SQLException{
		if(param instanceof Collection){
			return (Collection)param;
		}else if(param instanceof Object[]||ClassTypeUtils.isBasicArrayType(param.getClass())) {
			return new ArrayIterable<>(param);
		}else if(param instanceof Map){
			return (Collection)((Map)param).get(BatchParameterMap.batchParamsKey);
		}
		throw new SQLException("invalid parameter of batch method,must a collection or map has a key named "+BatchParameterMap.batchParamsKey);
	}
	
}
