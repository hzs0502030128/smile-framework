package org.smile.orm.mapping.adapter;

import org.smile.orm.mapping.OrmTableMapping;
import org.smile.orm.mapping.property.OrmProperty;

public class OrmTableMapperAdapter implements OrmMapperAdapter {

	public static OrmTableMapperAdapter instance=new OrmTableMapperAdapter();
	
	@Override
	public String getTableName(String objName) {
		OrmTableMapping mapper= OrmTableMapping.getType(objName);
		if(mapper!=null){
			return mapper.getName();
		}
		return null;
	}

	@Override
	public String getColumnName(String objName, String propertyName) {
		OrmTableMapping mapper=OrmTableMapping.getType(objName);
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
