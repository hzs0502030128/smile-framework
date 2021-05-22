package org.smile.reflect;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;

import org.smile.collection.CollectionUtils;

/**
 * 泛型封装     例：  &lt;String,Map&lt;String,List&lt;String>>  
 * 那么 values =  {String.class,Map.class} 
 * 	  subs  = 1 -> Map&lt;String,List&lt;String>> 的封装
 * @author 胡真山
 * @Date 2016年1月21日
 */
public class Generic {
	/**泛型的类数组*/
	public Class[] values;
	/**
	 * 子泛型的索引映射
	 */
	private Map<Integer,Generic> subs;
	/**
	 * 泛型元素的个数
	 * @param len
	 */
	public Generic(int len){
		values=new Class[len];
	}
	
	public Generic(Class... generics){
		this.values=generics;
	}
	/**
	 * 是否是空的泛型
	 * @param generic
	 * @return
	 */
	public static final boolean isEmpty(Generic generic){
		return generic==null||generic.isEmpty();
	}
	/**
	 * 泛型是否为空内容
	 * @return
	 */
	public boolean isEmpty(){
		return this.values==null||this.values.length==0||this.values[0]==null;
	}
	
	/**
	 * 设置 一个泛型元素
	 * @param index
	 * @param indexClass
	 */
	public void setIndex(int index,Class indexClass){
		values[index]=indexClass;
	}
	/**
	 * 
	 * @param index
	 * @param type
	 */
	public void setIndex(int index,ParameterizedType type){
		initSub();
		values[index]=(Class)type.getRawType();
		subs.put(index, ClassTypeUtils.getGenericObj(type));
	}
	
	protected void initSub(){
		if(subs==null){
			subs=new HashMap<Integer, Generic>();
		}
	}
	/**
	 * 设置子集的泛型
	 * @param index
	 * @param generic
	 */
	public Generic setSub(int index,Generic generic){
		initSub();
		subs.put(index, generic);
		return this;
	}
	/**
	 * 设置子级的泛型
	 * @param index
	 * @param generic
	 */
	public Generic setSub(int index,Class... generic){
		initSub();
		subs.put(index, new Generic(generic));
		return this;
	}
	
	/**
	 * 有没有存在子泛型
	 * @return
	 */
	public boolean hasSubGeneric(){
		return CollectionUtils.notEmpty(subs);
	}
	/**
	 * 子泛型
	 * @param index
	 * @return
	 */
	public Generic sub(int index){
		if(subs!=null){
			return subs.get(index);
		}
		return null;
	}
	/**
	 * 子泛型的class值
	 * @param index
	 * @return
	 */
	public Class[] subValues(int index){
		if(subs!=null){
			Generic g=subs.get(index);
			if(g!=null){
				return g.values;
			}
		}
		return null;
	}
}
