package org.smile.orm.base.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.smile.orm.mapping.OrmTableMapping;

public class OrmOtherObjInsertBounSql extends OrmTableBoundSql{

	public OrmOtherObjInsertBounSql(OrmTableMapping type, Object params) {
		super(type, params);
		this.sql=type.getInsertSql();
	}
	
	public OrmOtherObjInsertBounSql(OrmTableMapping type) {
		super(type);
		this.sql=type.getInsertSql();
	}

	@Override
	public void fillStatement(PreparedStatement ps) throws SQLException {
		filler.fillInsertOtherObjPreparedStatement(type, ps, getParams());
	}

	@Override
	public void fillBatchStatement(PreparedStatement ps, Object param) throws SQLException {
		filler.fillInsertOtherObjPreparedStatement(type, ps, param);
	}

}
