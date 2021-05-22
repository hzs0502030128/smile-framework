package org.smile.orm.base.impl;

import org.smile.db.sql.ArrayBoundSql;
import org.smile.db.sql.BoundSql;

public class BaseWhereSqlBoundBuilder implements WhereSqlBoundBuilder{
	
	@Override
	public BoundSql build(Class clazz,StringBuilder sql,String whereSql, Object[] params,Object[] newParams){
		return new ArrayBoundSql(sql.append(whereSql).toString(), newParams);
	}
	
}
