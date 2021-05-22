package org.smile.db.sql.parameter;

import java.util.HashMap;
/**
 * 批量操作的map参数 
 * @author 胡真山
 * 2015年10月21日
 */
public class BatchParameterMap extends HashMap<String,Object> {
	
	public static final String batchParamsKey="list";
	/**
	 * 批量操作时会获取key为list的值用于循环赋值
	 * @param batchList 批量操作的list 
	 */
	public BatchParameterMap(Object batchList){
		super.put(batchParamsKey,batchList);
	}
	
	@Override
	public Object put(String key, Object value) {
		if(batchParamsKey.equals(key)){
			throw new RuntimeException("不能添加一个key 名 称为"+batchParamsKey);
		}
		return super.put(key, value);
	}
	
	
	public void removeList(){
		remove(batchParamsKey);
	}
	
}
