
package org.smile.tag.impl;

import java.util.LinkedHashMap;
import java.util.Map;

import org.smile.commons.ann.Attribute;
import org.smile.tag.Scope;
import org.smile.tag.SimpleTag;
import org.smile.util.StringUtils;

/**
 * Map 标签  
 * @author 胡真山
 *
 */
public class MapTag extends SimpleTag{
	
	@Attribute
	private Object value;
	@Attribute
	private String var="map";
	@Attribute
	private Scope scope=Scope.page;
	/***
	 * map 内容
	 */
	private Map<Object,Object> mapValues;
	
	
	@Override
	public void doTag() throws Exception {
		if(StringUtils.isNull(value)){
			mapValues=new LinkedHashMap<Object,Object>();
		}else if(value instanceof Map){
			mapValues=(Map)value;
		}else if(value instanceof String){
			mapValues=TagUtils.parseToMap(value);
		}
		tagContext.setAttribute(var, mapValues, scope);
		invokeBody();
	}
	/**
	 * 添加内容到map中
	 * @param key
	 * @param value
	 */
	protected void putValue(Object key,Object value){
		this.mapValues.put(key, value);
	}

	@Override
	protected void reset() {
		super.reset();
		this.mapValues=null;
	}
	
	
}