package org.smile.db.sql.parameter;

import java.util.Map;

import org.smile.beans.converter.BeanException;
import org.smile.beans.handler.MapPropertyHandler;
import org.smile.commons.SmileRunException;

public  class MapParameterFiller extends MappingParameterFiller{
	/***
	 * 用于对map中的值进行获取
	 */
	private MapPropertyHandler mapHandler=new MapPropertyHandler();
	public MapParameterFiller(ParameterMapping pmList){
		super(pmList);
	}
	
	@Override
	protected Object getParamterValueForm(String name, Object value) {
		try {
			return mapHandler.getExpFieldValue((Map)value, name);
		} catch (BeanException e) {
			throw new SmileRunException(e);
		}
	}
}
