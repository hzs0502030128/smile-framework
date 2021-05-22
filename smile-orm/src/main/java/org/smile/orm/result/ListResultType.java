package org.smile.orm.result;

import java.util.List;

public class ListResultType extends BaseResultType{
	/**
	 * 泛型类型
	 */
	private Class genericType;
	
	public ListResultType(Class genericType){
		this.type=List.class;
		this.genericType=genericType;
	}
	
	@Override
	public Class getGenericType() {
		return genericType;
	}

}
