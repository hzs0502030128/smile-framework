package org.smile.function;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.smile.beans.BeanProperties;
import org.smile.beans.converter.BeanException;
import org.smile.beans.converter.type.IntegerConverter;
import org.smile.collection.ArrayUtils;
import org.smile.collection.CollectionUtils;
import org.smile.collection.Loop;
import org.smile.commons.SmileRunException;
import org.smile.expression.Engine;
import org.smile.expression.Expression;
import org.smile.reflect.ClassTypeUtils;
import org.smile.util.RegExp;
import org.smile.util.StringUtils;

/**
 * 一些常用的函数
 * 
 * @author 胡真山
 *
 */
public class CommonFunctions {
	/**
	 * 检验是否是null
	 * 
	 * @param check
	 * @param isNullResult
	 * @return
	 */
	public static Object ifnull(Object check, Object isNullResult) {
		if (check == null) {
			return isNullResult;
		}
		return check;
	}

	/**
	 * 
	 * @param exp
	 * @return
	 */
	public static RegExp regexp(String exp) {
		return new RegExp(exp);
	}

	/**
	 * 正则对象
	 * 
	 * @param exp
	 * @param iscased
	 *            是否区为大小写
	 * @return
	 */
	public static RegExp regexp(String exp, boolean iscased) {
		return new RegExp(exp, iscased);
	}

	/**
	 * 条件判断函数
	 * 
	 * @param test
	 * @param trueObj
	 * @param falseObj
	 * @return
	 */
	public static Object ifelse(boolean test, Object trueObj, Object falseObj) {
		if (test) {
			return trueObj;
		} else {
			return falseObj;
		}
	}

	/**
	 * 创建一个循环区间
	 * 
	 * @param start
	 * @param end
	 * @param step
	 * @return
	 */
	public static Loop range(int start, int end, int step) {
		return new Loop(start, end, step);
	}

	/**
	 * 循环区间
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public static Loop range(int start, int end) {
		return new Loop(start, end);
	}

	/**
	 * 创建一个list
	 * 
	 * @param arg
	 * @return
	 */
	public static List<Object> list(Object... arg) {
		return CollectionUtils.arrayList(arg);
	}
	
	/**
	 * 创建一个空数组
	 * @param len
	 * @return
	 */
	public static Object[] emptyarr(int len){
		return new Object[len];
	}

	/**
	 * 通用函数库使用 获取属性值
	 * @param obj
	 * @param key
	 * @return
	 */
	public static Object get(Object o, Object key) {
		try {
			if (o == null) {
				throw new NullPointerException("对象为空无法判断其长度");
			} else if (o instanceof Collection) {
				int index = IntegerConverter.instance.convert(key);
				return CollectionUtils.get((Collection) o, index);
			} else if (o instanceof Object[] || ClassTypeUtils.isBasicArrayType(o.getClass())) {
				int index = IntegerConverter.instance.convert(key);
				return ArrayUtils.get(o, index);
			} else if (o instanceof Map) {
				return ((Map) o).get(key);
			} else {
				return BeanProperties.NORAL.getFieldValue(o, String.valueOf(key));
			}
		} catch (BeanException e) {
			throw new SmileRunException(e);
		}
	}
	/**
	 * 通用函数库使用设置属性方法
	 * @param o
	 * @param key
	 * @param value
	 */
	public static void set(Object o,Object key,Object value){
		try {
			if (o == null) {
				throw new NullPointerException("can not set on null");
			}else if (o instanceof Object[] || ClassTypeUtils.isBasicArrayType(o.getClass())) {
				int index = IntegerConverter.instance.convert(key);
				Array.set(o, index,value);
			} else if (o instanceof Map) {
				((Map) o).put(key,value);
			} else {
				BeanProperties.NORAL.setExpFieldValue(o, String.valueOf(key),value);
			}
		} catch (BeanException e) {
			throw new SmileRunException(e);
		}
	}
	/**
	 * 连接成字符串
	 * @param array
	 * @param split
	 * @return
	 */
	public static String join(Object array,Object split){
		String splitc=split==null?"":String.valueOf(split);
		if(array instanceof Iterable){
			return StringUtils.join((Iterable)array, splitc);
		}else if(array instanceof Object[]){
			return StringUtils.join((Object[])array, splitc);
		}else if(ClassTypeUtils.isBasicArrayType(array.getClass())){
			int len=Array.getLength(array);
			StringBuilder strs = new StringBuilder(len*16);
			for (int i=0;i<len;i++) {
				strs.append(split).append(Array.get(array, i));
			}
			return strs.substring(splitc.length());
		}
		return array.toString();
	}
	/**
	 * 构建一个表达式对象
	 * @param expression
	 * @return
	 */
	public static Expression expression(String expression) {
		return Engine.getInstance().parseExpression(expression);
	}
}
