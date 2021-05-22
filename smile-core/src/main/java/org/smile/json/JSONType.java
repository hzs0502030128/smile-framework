package org.smile.json;

import java.util.Set;

import org.smile.collection.CollectionUtils;

public enum JSONType {
	/**对象类型*/
	OBJECT,
	/**数组类型*/
	ARRAY,
	/***/
	DOUBLE,
	INTEGER,
	LONG,
	STRING,
	TRUE,
	FALSE,
	/**boolean类型*/
	BOOLEAN(FALSE,TRUE),
	/**数字类型*/
	NUMBER(DOUBLE,INTEGER,LONG),
	/**基本类型封装的json*/
	BASIC(BOOLEAN,STRING,NUMBER),
	/**空json*/
	NULL;
	/**子类型**/
	private Set<JSONType> subType;
	/**
	 * @param jsonTypes 子类型
	 */
	private JSONType(JSONType...jsonTypes ){
		this.subType=CollectionUtils.hashSet(jsonTypes);
	}
	/***
	 * 是否可以当成此类型
	 * @param jsonType
	 * @return
	 */
	public boolean isAssignableFrom(JSONType jsonType){
		if(jsonType==this){
			return true;
		}else if(hasSubType()){
			if(subType.contains(jsonType)){
				return true;
			}
			for(JSONType sub:subType){
				if(sub.isAssignableFrom(jsonType)){
					return true;
				}
			}
		}
		return false;
	}
	/**是否存在子类型*/
	protected boolean hasSubType(){
		return subType != null;
	}
}
