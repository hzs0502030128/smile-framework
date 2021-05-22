package org.smile.orm.result;

import org.smile.reflect.ClassTypeUtils;
/**
 * 
 * @author 胡真山
 *
 */
public class ResultTypeUtils {
	/**
	 * 判断该类型是否是map类型
	 * @param type
	 * @return
	 */
	public static boolean isMapType(String type){
		return ClassTypeUtils.isMapName(type);
	}
	/**
	 * 是否是字符串类型
	 * @param type
	 * @return
	 */
	public static boolean isStringType(String type){
		return ClassTypeUtils.isStringName(type);
	}
}
