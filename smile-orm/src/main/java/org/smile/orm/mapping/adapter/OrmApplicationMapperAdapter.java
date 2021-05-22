package org.smile.orm.mapping.adapter;

import org.smile.orm.OrmApplication;
import org.smile.orm.mapping.OrmTableMapping;
import org.smile.orm.mapping.property.OrmProperty;

public class OrmApplicationMapperAdapter implements OrmMapperAdapter {

	private OrmApplication application;
	
	public OrmApplicationMapperAdapter(OrmApplication application){
		this.application=application;
	}
	@Override
	public String getTableName(String objName) {
		OrmTableMapping<?> mapper= application.getTableMapper(objName);
		if(mapper!=null){
			return mapper.getName();
		}
		return null;
	}

	@Override
	public String getColumnName(String objName, String propertyName) {
		OrmTableMapping<?> mapper=application.getTableMapper(objName);
		if(mapper==null){
			return null;
		}
		OrmProperty p= mapper.getProperty(propertyName);
		if(p==null){
			return null;
		}
		return p.getColumnName();
	}

}
