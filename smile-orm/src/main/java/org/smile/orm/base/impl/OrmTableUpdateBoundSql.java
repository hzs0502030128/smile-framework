package org.smile.orm.base.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;

import org.smile.orm.mapping.OrmTableMapping;
import org.smile.orm.mapping.property.OrmFieldProperty;
import org.smile.orm.mapping.property.OrmProperty;
/**
 * 用于ORM更新是封装数据
 * @author 胡真山
 * @Date 2016年2月26日
 */
public class OrmTableUpdateBoundSql extends OrmTableBoundSql {
	
	protected Collection<OrmProperty> properties;
	
	public OrmTableUpdateBoundSql(String sql,OrmTableMapping type,Collection<OrmProperty> properties) {
		super(type);
		this.properties=properties;
		this.sql=sql;
	}
	
	public OrmTableUpdateBoundSql(String sql,OrmTableMapping type,Object params,Collection<OrmProperty> properties) {
		super(type, params);
		this.properties=properties;
		this.sql=sql;
	}

	@Override
	public void fillStatement(PreparedStatement ps) throws SQLException {
		filler.fillUpdateByIdPreparedStatement(type, ps, getParams(),properties);
	}

	@Override
	public void fillBatchStatement(PreparedStatement ps, Object param) throws SQLException {
		filler.fillUpdateByIdPreparedStatement(type, ps, param,properties);
	}

	@Override
	public Iterator<OrmProperty> iteratorMapping() {
		return properties.iterator();
	}
	
	
}
