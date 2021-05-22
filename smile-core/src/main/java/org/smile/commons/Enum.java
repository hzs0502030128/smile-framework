package org.smile.commons;

import java.util.Map;
/**
 * 可以用于枚举标签的接口
 * 此方法显示的Map 
 * 在options标签中使用 就会把键值对生成一个option HTML标签
 * @author 胡真山
 *
 */
public  interface Enum {
	/**
	 * 此方法显示的Map 
	 * 在options标签中使用 就会把键值对生成一个option HTML标签
	 * @author strive
	 *
	 */
	public abstract Map getDataMap();
}
