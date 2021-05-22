package org.smile.orm.base.impl;

import org.smile.orm.mapping.OrmObjMapping;

public class OrmObjRowHandler extends OrmTableRowHandler{
	
	public OrmObjRowHandler(Class mapperClass){
		this.resultClass=mapperClass;
		this.ormMapping=OrmObjMapping.getOrmMapper(mapperClass);
	}
	
	public OrmObjRowHandler(OrmObjMapping mapping) {
		this.resultClass=mapping.getRawClass();
		this.ormMapping=mapping;
	}
	
	@Override
	public boolean needDoRelate() {
		return false;
	}
	
}
