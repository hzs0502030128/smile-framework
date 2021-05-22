
package org.smile.tag.impl;

import org.smile.commons.ann.Attribute;
import org.smile.tag.SimpleTag;
import org.smile.tag.Tag;

/**
 * url tag 内部标签的父类
 * @author 胡真山
 *
 */
public class MapNodeTag extends SimpleTag{
	@Attribute
	private Object key;
	@Attribute
	private Object value;
	
	@Override
	public void doTag() throws Exception {
		MapTag tag=getMapTag();
		if(tag==null){
			throw new TagException("can not find a parent tag of MapTag");
		}
		tag.putValue(key, value);
	}
	
	public MapTag getMapTag(){
		Tag pTag=parent;
		while(pTag!=null){
			if(pTag instanceof MapTag){
				return (MapTag)pTag;
			}
			pTag=pTag.getParent();
		}
		return null;
	}
}