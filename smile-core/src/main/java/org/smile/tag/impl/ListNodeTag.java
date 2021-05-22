
package org.smile.tag.impl;

import java.util.Collection;

import org.smile.commons.ann.Attribute;
import org.smile.tag.SimpleTag;
import org.smile.tag.Tag;

public class ListNodeTag extends SimpleTag{
	@Attribute
	private Object value;
	/***
	 * 是否是集合类型
	 */
	@Attribute
	private boolean collection=false;
	
	@Override
	public void doTag() throws Exception {
		ListTag tag=getListTag();
		if(tag==null){
			throw new TagException("can not find a parent tag of ListTag");
		}
		if(collection&&value instanceof Collection){
			tag.addNodes((Collection)value);
		}else{
			tag.addNode(value);
		}
	}
	
	public ListTag getListTag(){
		Tag pTag=parent;
		while(pTag!=null){
			if(pTag instanceof ListTag){
				return (ListTag)pTag;
			}
			pTag=pTag.getParent();
		}
		return null;
	}
	
}