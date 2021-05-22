
package org.smile.tag.impl;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.smile.commons.ann.Attribute;
import org.smile.json.JSON;
import org.smile.tag.Scope;
import org.smile.tag.SimpleTag;
import org.smile.util.StringUtils;

/**
 * list 标签  
 * @author 胡真山
 *
 */
public class ListTag extends SimpleTag{
	
	public static final String LIST_TAG_FLAG="smile_list_tag_flag_parent";
	@Attribute
	private Object value;
	@Attribute
	private String var="list";
	@Attribute
	protected Scope scope=Scope.page;
	//list的内容,可以子标签中添加元素
	private List listValues;
	
	
	@Override
	public void doTag() throws Exception {
		if(StringUtils.isNull(value)){
			listValues=new LinkedList();
		}else if(value instanceof List){
			listValues=(List)value;
		}else if(value instanceof String){
			Object jsonValue= JSON.parse((String)value);
			if(jsonValue instanceof List){
				listValues=(List)jsonValue;
			}else{
				throw new TagException("value: 错误 "+value);
			}
		}
		tagContext.setAttribute(var, listValues,scope);
		invokeBody();
	}
	
	protected void addNode(Object node){
		listValues.add(node);	
	}
	
	protected void addNodes(Collection nodes){
		listValues.addAll(nodes);	
	}


	@Override
	protected void reset() {
		super.reset();
		this.listValues=null;
	}
	
	
	
}