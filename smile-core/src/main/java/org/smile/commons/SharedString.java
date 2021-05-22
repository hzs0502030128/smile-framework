package org.smile.commons;

import java.lang.reflect.Constructor;
/**
 * 用于共享char[] 构建一个字符串  
 * 使用此方法必须确保数组一会被修改
 * @author 胡真山
 */
public final class SharedString implements Comparable<SharedString>{
	/**共享数组的字符串*/
	private final String value;
	//是否支持shared
	private  static boolean support=true;
	/**共享char数组*/
	public SharedString(char[] values){
		if(support){
			this.value=shareValueOf(values);
		}else{
			this.value=new String(values);
		}
	}
	/**
	 * 共享数组的字符串
	 * @param value
	 * @return
	 */
	protected final String shareValueOf(char[] value){
		try{
			Constructor<String> constructor=String.class.getDeclaredConstructor(char[].class,boolean.class);
			constructor.setAccessible(true);
			String result=constructor.newInstance(value,true);
			return result;
		}catch(Exception e){
			support=false;
			return new String(value);
		}
	}
	
	@Override
	public int hashCode() {
		return value.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this==obj){
			return true;
		}else if(obj instanceof String){
			return value.equals(obj);
		}else if(obj instanceof SharedString){
			return value.equals(obj.toString());
		}
		return false;
	}
	
	@Override
	public String toString() {
		return value;
	}
	
	@Override
	public int compareTo(SharedString o) {
		return value.compareTo(o.value);
	}
}
