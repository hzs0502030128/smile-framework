package org.smile.beans.property;

import org.smile.util.StringUtils;


public class LikeKeyUtil {
	/**数据库字段命名分隔*/
	static final char split='_';
	/***
	 * 从属性名转成数据库字段
	 * @param name
	 * @return
	 */
	public static String nameToKey(String name){
		int len=name.length();
		char[] array=new char[(int)(len*1.5)];
		int srcBegin=0;
		int dstBegin=0;
		boolean isUpper=false;
		int flagCount=0;
		for(int i=1;i<len;i++){
			char c=name.charAt(i);
			boolean b=isUpperChar(c);
			if(!isUpper&&b){
				name.getChars(srcBegin, i, array, dstBegin);
				srcBegin=i;
				dstBegin=i+flagCount;
				flagCount++;
				array[dstBegin]=split;
				dstBegin++;
			}
			isUpper=b;
		}
		name.getChars(srcBegin, len, array, dstBegin);
		return new String(array, 0, len+flagCount);
	}
	
	/**
	 * 转变成like map的键   
	 * 去掉 '_'  转成小写
	 * @param key
	 * @return
	 */
	public static String toLikeMapKey(String key){
		return StringUtils.remove(key, split).toLowerCase();
	}
	/**
	 * 是否是大写字母
	 * @param c
	 * @return
	 */
	private static boolean isUpperChar(char c){
		return 'A'<=c && c<='Z';
	}
	
}
