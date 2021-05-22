package org.smile.collection;

import org.smile.beans.property.LikeKeyUtil;

/**
 * 可以用不分大小写的键获取元素    忽略下划线
 * 键存放是有顺序的 
 * @author 胡真山
 * @Date 2016年1月25日
 * @param <V>
 */
public class KeyLikeHashMap<V> extends KeyNoCaseHashMap<V>{
	
    public KeyLikeHashMap(){
    	super();
    }
    
    public KeyLikeHashMap(int size){
    	super(size);
    }
    
    protected String convertKey(Object key){
    	String mapKey=String.valueOf(key);
    	return LikeKeyUtil.toLikeMapKey(mapKey);
    }
   
}
