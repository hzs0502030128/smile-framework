package org.smile.jstl.functions;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.smile.util.RegExp;
import org.smile.util.StringUtils;

/**
 * 字符串函数
 * 
 * @author 胡真山
 *
 */
public class StringFunction {
	
	/**
	 * 字符串取函数
	 * @param str
	 * @param start 起始索引
	 * @param end 结束索引
	 * @return
	 */
	public static String substring(String str,int start,int end){
		return StringUtils.substring(str, start, end);
	}
	/**
	 * 
	 * @param str
	 * @param start 起始索引
	 * @param length 长度
	 * @return
	 */
	public static String substr(String str,int start,int length){
		return StringUtils.substr(str, start, length);
	}
	/**
	 * 反加子字符串的索引
	 * @param str
	 * @param index
	 * @return
	 */
	public static int indexOf(String str,String index){
		return str.indexOf(index);
	}
	/**
	 * 反回最后一个子字符串的索引
	 * @param str
	 * @param index
	 * @return
	 */
	public static int lastIndexOf(String str,String index){
		return str.lastIndexOf(index);
	}
	/**
	 * 替换字符串
	 * @param src 源字符串
	 * @param newStr 新内容
	 * @param reg 要替换的正则
	 * @return 新的字符串
	 */
	public static String replaceAll(String src,String newStr,String reg){
		Pattern p = Pattern.compile(reg); 
		Matcher m=p.matcher(src);
		return m.replaceAll(newStr);
	}
	/**
	 * 把一个字符串分成一个数组
	 * @param src
	 * @param reg
	 * @return
	 */
	public static String[] split(String src,String reg){
		return new RegExp(reg).split(src);
	}
	/**
	 * 查找从一个字符串中
	 * @param src 查找目标字符串
	 * @param reg 查找匹配正则
	 * @return
	 */
	public static String[] find(String src,String reg){
		return new RegExp(reg).find(src);
	}
	/**
	 * 正则表达式是否匹配字符串
	 * @param reg
	 * @param src
	 * @return
	 */
	public static boolean test(String reg,String src){
		return new RegExp(reg).test(src);
	}
	/**
	 * 省略一个字符串
	 * @param src
	 * @param length 省略后的长度
	 * @return
	 */
	public static String omit(String src,int length){
		if(src==null) return src;
		int len=src.length();
		if(len>length){
			return src.substring(0,length)+"…";
		}
		return src;
	}
	/**
	 * 左边省略
	 * @param src
	 * @param length
	 * @return
	 */
	public static String lomit(String src,int length){
		if(src==null) return src;
		int len=src.length();
		if(len>length){
			int s=len-length;
			return "…"+src.substring(s,len);
		}
		return src;
	}
	/**
	 * 省略一个字符串
	 * @param src
	 * @param length 省略后的长度
	 * @return
	 */
	public static String omit(String src,int length,String descript){
		if(src==null) return src;
		int len=src.length();
		if(len>length){
			if(descript.indexOf("{0}")>=0){
				return src.substring(0,length)+descript.replaceAll("\\{0\\}",len-length+"");
			}else{
				return src.substring(0,length)+descript;
			}
		}
		return src;
	}
}
