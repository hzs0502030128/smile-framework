package org.smile.orm.base.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;

import org.smile.orm.mapping.OrmTableMapping;
import org.smile.orm.mapping.property.OrmProperty;

public class OrmOtherObjUpdateBoundSql extends OrmTableUpdateBoundSql{

	public OrmOtherObjUpdateBoundSql(String sql, OrmTableMapping type, Collection<OrmProperty> properties) {
		super(sql, type, properties);
	}

	public OrmOtherObjUpdateBoundSql(String string, OrmTableMapping pType, Object updateObj, Collection<OrmProperty> propertys) {
		super(string, pType, updateObj, propertys);
	}

	@Override
	public void fillStatement(PreparedStatement ps) throws SQLException {
		filler.fillOtherObjUpdateByIdPreparedStatement(type, ps, getParams(), properties);
	}

	@Override
	public void fillBatchStatement(PreparedStatement ps, Object param) throws SQLException {
		filler.fillOtherObjUpdateByIdPreparedStatement(type, ps, param, properties);
	}

	
	
}
