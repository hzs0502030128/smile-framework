package org.smile.db.jdbc;

import java.util.Map;

import org.smile.db.handler.MapRowHandler;

public class RecordMapRowHandler extends MapRowHandler{
	
	protected TableInfoCfg cfg;
	
	protected RecordMapRowHandler(TableInfoCfg cfg) {
		this.cfg=cfg;
	}
	
	@Override
	protected Map<String, Object> buildMap() {
		return new JdbcMapRecord(cfg);
	}

	@Override
	public Class getResultClass() {
		return JdbcMapRecord.class;
	}
	
}
