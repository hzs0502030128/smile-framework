package org.smile.strate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.smile.collection.CollectionUtils;
/**
 * 用于封装request中的参数
 * @author strive
 *
 */
public class RequestParameter extends HashMap<String,List>{
	/**
	 * 添加一个参数
	 * @param name
	 * @param value
	 */
	public synchronized void addParam(String name,Object value){
		if(containsKey(name)){
			List list=this.get(name);
			list.add(value);
		}else{
			List list=new ArrayList();
			list.add(value);
			this.put(name, list);
		}
	}
	
	/**
	 * 设置一项参数
	 * @param name
	 * @param values
	 */
	public void setValus(String name,List values){
		this.put(name, values);
	}
	/**
	 * 
	 * @param name
	 * @return
	 */
	public synchronized Object[] getParameters(String name){
		List values=this.get(name);
		if(values!=null){
			return values.toArray();
		}else {
			return new Object[]{};
		}
	}
	/**普通字段参数
	 * 得到
	 * @param name
	 * @return
	 */
	public String[] getFieldValues(String name){
		List values=this.get(name);
		String[] strs=new String[values.size()];
		for(int i=0;i<values.size();i++){
			strs[i]=(String)values.get(i);
		}
		return strs;
	}
	
	public synchronized String getParamter(String name){
		List list=get(name);
		if(CollectionUtils.isEmpty(list)){
			return null;
		}else{
			return (String)list.get(0);
		}
	}
}
