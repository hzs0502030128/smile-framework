package org.smile.orm.plugin;
/**
 * top查询
 * @author 胡真山
 */
public class TopHelper {
	
	private static final ThreadLocal<Integer> topParam=new ThreadLocal<Integer>();
	/**
	 * 设置top查询
	 * @param top
	 */
	public static void startTop(int top){
		topParam.set(top);
	}
	
	public static void remove(){
		topParam.remove();
	}
	/**
	 * top 查询 
	 * @return
	 */
	public static Integer getTop(){
		return topParam.get();
	}
}
