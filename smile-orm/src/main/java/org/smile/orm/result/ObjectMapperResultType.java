package org.smile.orm.result;

import org.smile.orm.mapping.OrmObjMapping;

public class ObjectMapperResultType extends ObjectResultType{
	/**对象*/
	private OrmObjMapping resultMapper;
	
	public ObjectMapperResultType(OrmObjMapping mapper){
		super(mapper.getRawClass());
		this.resultMapper=mapper;
	}

	public OrmObjMapping getResultMapper() {
		return resultMapper;
	}

	@Override
	public Class getGenericType() {
		return resultMapper.getRawClass();
	}
	
}
