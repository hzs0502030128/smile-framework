package org.smile.orm.base.impl;

import java.sql.SQLException;

import org.smile.db.handler.RowHandler;
import org.smile.db.result.BeanResultParser;
import org.smile.db.result.QueryResult;
import org.smile.db.result.ResultParser;
import org.smile.orm.mapping.OrmMapping;

public class MixRowHandler<V> extends RowHandler{
	/**结果集解析*/
	protected static ResultParser parser = new BeanResultParser();
	
	protected OrmMapping<V> mixMapping;
	
	protected V mixTarget;
	
	public MixRowHandler(OrmMapping<V> mixMapping,V target) {
		this.mixMapping=mixMapping;
		this.mixTarget=target;
	}
	
	@Override
	public V handle(QueryResult rs) throws SQLException {
		V bean;
		try {
			bean = mixMapping.parseResultSet(parser,this.mixTarget, rs);
		} catch (Exception e) {
			throw new SQLException("转换结果集成对象错误：" + resultClass + " " + e.getMessage(), e);
		}
		return bean;
	}

}
