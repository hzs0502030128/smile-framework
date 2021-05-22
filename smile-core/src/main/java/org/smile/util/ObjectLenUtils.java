package org.smile.util;

import java.lang.reflect.Array;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;

import org.smile.collection.ArrayUtils;
import org.smile.collection.CollectionUtils;
import org.smile.reflect.ClassTypeUtils;
/**
 * 对象长度获取工具类
 * @author strive
 *
 */
public class ObjectLenUtils {
	/**
	 * 是不是一样有长度的对象
	 * @param o 对象 数组 Collection
	 * @return 整型长度
	 */
	public static  boolean hasLength(Object o){
		if(o==null){
			throw new NullPointerException("对象为空无法判断其长度");
		}else if(o instanceof Collection){
			return true;
		}else if(o instanceof Object[]){
			return true;
		}else{
			return ClassTypeUtils.isBasicArrayType(o.getClass());
		}
	}
	/**
	 * 得到参数的对应索引的值
	 * @param o
	 * @param index
	 * @return
	 * @throws SQLException
	 */
	public static <T> T get(Object o,int index){
		if(o==null){
			throw new NullPointerException("对象为空无法判断其长度");
		}else if(o instanceof Collection){
			return (T)CollectionUtils.get((Collection)o, index);
		}else{
			return (T)ArrayUtils.get(o, index);
		}
	}
	/**
	 * 得到一个对象的长度
	 * @param o
	 * @return
	 */
	public static int len(Object o){
		if(o==null){
			throw new NullPointerException("对象为空无法判断其长度");
		}else if(o instanceof Collection){
			return ((Collection) o).size();
		}else if(o instanceof Map){
			return ((Map)o).size();
		}else{
			return Array.getLength(o);
		}
	}
}
