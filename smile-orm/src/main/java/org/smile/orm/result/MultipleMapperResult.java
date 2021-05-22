package org.smile.orm.result;

import java.util.HashMap;

import java.util.LinkedHashMap;

public class MultipleMapperResult extends BaseResultType implements MultipleMapperSupport{

	private HashMap<String,Class> mapperClass=new LinkedHashMap<String,Class>();
	
	@Override
	public Class getGenericType() {
		return null;
	}

	@Override
	public Class getMapperClass(String shortCut) {
		return mapperClass.get(shortCut);
	}

}
