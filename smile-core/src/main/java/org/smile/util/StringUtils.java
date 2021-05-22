/**
 * @auther huzhenshan
 * @date 2014-10-28
 */
package org.smile.util;

import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.smile.Smile;
import org.smile.collection.ArrayUtils;
import org.smile.commons.Chars;
import org.smile.commons.SharedString;
import org.smile.commons.Strings;
import org.smile.template.StringTemplateParser;
import org.smile.template.StringTemplateParser.BaseMacroResolver;
import org.smile.template.TemplateParser.MacroResolver;

/**
 * 字符串操作工具类
 * 
 * @author 胡真山
 * @Date 2016年1月12日
 */
public class StringUtils{
	/**
     * <p>The maximum size to which the padding constant(s) can expand.</p>
     */
    private static final int PAD_LIMIT = 8192;
	
	/**对占位符进行替换*/
	private static StringTemplateParser replaceParer=new StringTemplateParser("{","}",true);
	/**模板解析式*/
	private static StringTemplateParser templateParer=new StringTemplateParser("${","}",true);
	/***
	 * 是否是数字类型的字符
	 * 
	 * @param c 0-9
	 * @return
	 */
	public static boolean isNumberChar(char c) {
		return CharUtils.isDigit(c);
	}

	/***
	 * 去空格
	 * 如果为  <code>null</code> 不处理直接返回 <code>null</code>
	 * 不为 <code>null</code> 调用 {@link String} 的 trim 方法
	 * @param str 去字符两边空格
	 * @return
	 */
	public static String trim(String str) {
		if (isEmpty(str)) {
			return str;
		}
		return str.trim();
	}

	/**
	 * 去空格后如果是空字符串  转为null
	 * @param str
	 * @return
	 */
	public static String trimToNull(String str) {
		String ts = trim(str);
		return isEmpty(ts) ? null : ts;
	}

	/**
	 * 移除字符串
	 * @param str
	 * @param remove
	 * @return
	 */
	public static String remove(String str, String remove) {
		if ((isEmpty(str)) || (isEmpty(remove))) {
			return str;
		}
		return str.replaceAll(remove, Strings.BLANK);
	}
	/**
	 * 从右取字符个数
	 * @param str
	 * @param len 新字符长度
	 * @return
	 */
	public static String right(String str,int len){
		if(str==null||str.length()==0){
			return str;
		}
		return str.substring(str.length()-len);
	}
	
	/**
	 * 从左取字符个数
	 * @param str
	 * @param len 新字符长度 长度不够时会抛出异常
	 * @return
	 */
	public static String left(String str,int len){
		if(str==null||str.length()==0){
			return str;
		}
		return str.substring(0,len);
	}
	/**
	 * 对字符串长度进行限限截取
	 * @param str
	 * @param offset
	 * @param len 长度不够时取到字符串结尾
	 * @return
	 */
	public static String limit(String str,int offset,int len) {
		if(str==null||str.length()==0){
			return str;
		}
		int end=offset+len;
		if(end>str.length()) {
			if(offset==0) {
				return str;
			}
			end=str.length();
		}
		return str.substring(offset, end);
	}
	/**
	 * 对一个字符串进行省略操作
	 * @param str
	 * @param len
	 * @return
	 */
	public static String omit(String str,int len) {
		if(str==null||str.length()==0){
			return str;
		}
		int end=len;
		if(end>=str.length()) {
			return str;
		}
		int omit=str.length()-len;
		StringBuilder res=new StringBuilder(len+20);
		res.append(str.substring(0, len));
		res.append("[省略"+omit+"字]");
		return res.toString();
	}
	
	
	
	/**
	 * 限限制字符串的长度
	 * @param str
	 * @param len
	 * @return
	 */
	public static String limit(String str,int len) {
		return limit(str, 0, len);
	}
	
	/**
	 * 反换字符串
	 * @param str
	 * @return
	 */
	public static String reverse(String str){
		char[] chrs=str.toCharArray();
		ArrayUtils.reverse(chrs);
		return new String(chrs);
	}

	/**
	 * 移除字符
	 * @param str
	 * @param remove
	 * @return
	 */
	public static String remove(String str, char remove) {
		if (isEmpty(str)) {
			return str;
		}
		char[] chars = str.toCharArray();
		int pos = 0;
		for (int i = 0; i < chars.length; i++) {
			if (chars[i] != remove) {
				chars[(pos++)] = chars[i];
			}
		}
		return new String(chars, 0, pos);
	}

	/**
	 * 如果是null 转为空字符串
	 * @param str
	 * @return
	 */
	public static String trimToBlank(String str) {
		return str == null ? Strings.BLANK : str.trim();
	}

	/**
	 * 左去空格 
	 * 
	 * @param str
	 * @return
	 */
	public static String ltrim(String str) {
		if (isEmpty(str)) {
			return str;
		}
		int len = str.length();
		for (int i = 0; i < len; i++) {
			if (Chars.SPACE<str.charAt(i)) {
				return str.substring(i);
			}
		}
		return Strings.BLANK;
	}
	
	/**
	 * Trim whitespaces from the left.
	 */
	public static String trimLeft(String src) {
		if(src==null) {
			return null;
		}
		int len = src.length();
		int st = 0;
		while ((st < len) && (src.charAt(st)<=Chars.SPACE)) {
			st++;
		}
		return st > 0 ? src.substring(st) : src;
	}

	/**
	 * Trim whitespaces from the right.
	 */
	public static String trimRight(String src) {
		if(src==null){
			return null;
		}
		int len = src.length();
		int count = len;
		while ((len > 0) && (src.charAt(len - 1))<=Chars.SPACE) {
			len--;
		}
		return (len < count) ? src.substring(0, len) : src;
	}

	/**
	 * 空字字符串转成<code>null</code>
	 * "" --> <code>null</code>
	 * @param str 要转换的字符串
	 * @return 如果是"" 返回<code>null</code> 否则返回 str
	 */
	public static String blankToNull(String str) {
		if (isEmpty(str)) {
			return null;
		}
		return str;
	}

	/**
	 * trim之后是否为空
	 * @param str
	 * @return
	 */
	public static boolean isEmptyAfterTrim(String str) {
		if (str == null) {
			return true;
		}
		return str.trim().length()==0;
	}
	/**
	 * 是否这空或空字符串
	 * @param chars
	 * @return
	 */
	public static boolean isEmpty(CharSequence chars) {
		return chars == null || chars.length() == 0;
	}
	/**
	 * 是空为null 空串  或者    空格    
	 * @param str
	 * @return
	 */
	public static boolean isBlank(CharSequence str) {
        int strLen;
        if (str != null && (strLen = str.length()) != 0) {
            for(int i = 0; i < strLen; ++i) {
                // 判断字符是否为空格、制表符、tab
                if (!Character.isWhitespace(str.charAt(i))) {    
                    return false;
                }
            }
            return true;
        } else {
            return true;
        }
	}
	
	/**
	 *    不是null 空串  和  空格
	 * @param str
	 * @return
	 */
	public static boolean notBlank(CharSequence str) {
		return !isBlank(str);
	}
	
	/**
	 *    不是null 空串  和  空格
	 * @param str
	 * @return
	 */
	public static boolean isNotBlank(CharSequence str) {
		return !isBlank(str);
	}

	/**
	 * 不是空或空字符串
	 * @param chars
	 * @return
	 */
	public static boolean notEmpty(CharSequence chars) {
		return !isEmpty(chars);
	}

	/**
	 * 以字符验证长度 参考 {@link String } length 方法
	 * @param str 字符串
	 * @param min 最小长度
	 * @param max 最大长度
	 * @return 如果是空返回false 否则返回是否在区间内
	 */
	public static boolean checkCharLen(String str, int min, int max) {
		if (isEmpty(str)) {
			return false;
		}
		int len = str.length();
		if (len < min || len > max) {
			return false;
		}
		return true;
	}

	/** 以字节验证长度 */
	public static boolean checkByteLen(String str, int min, int max) {
		if (isEmpty(str)) {
			return false;
		}
		byte[] bytes;
		try {
			bytes = str.getBytes(Smile.ENCODE);
		} catch (UnsupportedEncodingException e) {
			bytes = str.getBytes();
		}
		if (bytes.length < min || bytes.length > max) {
			return false;
		}
		return true;
	}

	/**
	 * 连接成字符串
	 * 
	 * @param str
	 * @param split 连接的分隔符
	 * @return
	 */
	public static String join(Object[] str, char split) {
		if (ArrayUtils.isEmpty(str)) {
			return Strings.BLANK;
		}
		StringBuilder strs = new StringBuilder();
		for (Object s : str) {
			strs.append(s).append(split);
		}
		return strs.substring(0, strs.length() - 1);
	}

	/**
	 * 连接成字符串
	 * 
	 * @param str
	 * @param split 连接的分隔符
	 * @return
	 */
	public static String join(Object[] str, String split) {
		if(str==null){
			return null;
		}
		return join(str, split, 0, str.length);
	}
	
	/**
	 * 连接成字符串
	 * 
	 * @param str
	 * @param split 连接的分隔符
	 * @return
	 */
	public static String join(Object[] str, String split,int startIndex,int endIndex) {
		if (str==null) {
			return null;
		}
		int len=endIndex-startIndex;
		if(len<=0){
			return Strings.BLANK;
		}
		StringBuilder strs = new StringBuilder(len*16);
		for (int i=startIndex;i<endIndex;i++) {
			strs.append(split).append(str[i]);
		}
		return strs.substring(split.length());
	}

	/**
	 * 连接成字符串
	 * 
	 * @param iterable 为空时返回空空字符串
	 * @param split
	 * @return
	 */
	public static String join(Iterable<?> iterable, char split) {
		if (iterable == null){
			return Strings.BLANK;
		}
		StringBuilder strs = new StringBuilder();
		Iterator<?> list = iterable.iterator();
		while (list.hasNext()) {
			strs.append(list.next()).append(split);
		}
		if (strs.length() > 0) {
			return strs.substring(0, strs.length() - 1);
		} else {
			return strs.toString();
		}
	}
	/**
	 * 连接成一个字符串
	 * @param objects
	 * @return
	 */
	public static String join(Object... objects){
		return join(objects,Strings.BLANK);
	}

	/**
	 * 连接成字符串
	 * 
	 * @param iterable 为空时返回空空字符串
	 * @param split
	 * @return
	 */
	public static String join(Iterable<?> iterable, String split) {
		if (iterable == null){
			return Strings.BLANK;
		}
		StringBuilder strs = new StringBuilder();
		Iterator<?> list = iterable.iterator();
		while (list.hasNext()) {
			strs.append(split).append(list.next());
		}
		if (strs.length() > 0) {
			return strs.substring(split.length());
		} else {
			return strs.toString();
		}
	}

	/**
	 * 首字母小写
	 * 
	 * @param name
	 * @return
	 */
	public static String getFirstCharLow(String name) {
		return name.substring(0, 1).toLowerCase() + name.substring(1);
	}

	/**
	 * 首字母大写
	 * @param name
	 * @return
	 */
	public static String getFirstCharUpper(String name) {
		return name.substring(0, 1).toUpperCase() + name.substring(1);
	}

	/**
	 * 	首字母大写
	 * 
	 * @param name
	 * @return
	 */
	public static String capitalize(String name) {
		return name.substring(0, 1).toUpperCase() + name.substring(1);
	}

	/***
	 * 是否为空  或 ""
	 * @param obj
	 * @return
	 */
	public static boolean isNull(Object obj) {
		if (obj == null) {
			return true;
		} else if (obj instanceof String) {
			return Strings.BLANK.equals(obj);
		}
		return false;
	}

	/**
	 * 不为空 或 ""
	 * @param obj
	 * @return
	 */
	public static boolean isNotNull(Object obj) {
		return !isNull(obj);
	}

	/***
	 * 拆分字符串
	 * 
	 * @param src
	 * @param split
	 * @return
	 */
	public static String[] split(String src, String split) {
		if (src.length()==0||split.length()==0) {
			return new String[] { src };
		}
		return src.split(split);
	}


	/**
	 * 给定的字符数组中的任务一个字符都可以做为分隔
	 * @param src
	 * @param delimiters
	 * @return
	 */
	public static String[] splitc(String src, char[] delimiters) {
		if ((delimiters.length == 0) || (src.length() == 0)) {
			return new String[] { src };
		}
		char[] srcc = src.toCharArray();

		int maxparts = srcc.length + 1;
		int[] start = new int[maxparts];
		int[] end = new int[maxparts];

		int count = 0;

		start[0] = 0;
		int s = 0, e;
		if (CharUtils.equalsOne(srcc[0], delimiters) == true) { // string starts
																// with
																// delimiter
			end[0] = 0;
			count++;
			s = CharUtils.findFirstDiff(srcc, 1, delimiters);
			if (s == -1) { // nothing after delimiters
				return new String[] { Strings.EMPTY, Strings.EMPTY };
			}
			start[1] = s; // new start
		}
		while (true) {
			// find new end
			e = CharUtils.findFirstEqual(srcc, s, delimiters);
			if (e == -1) {
				end[count] = srcc.length;
				break;
			}
			end[count] = e;

			// find new start
			count++;
			s = CharUtils.findFirstDiff(srcc, e, delimiters);
			if (s == -1) {
				start[count] = end[count] = srcc.length;
				break;
			}
			start[count] = s;
		}
		count++;
		String[] result = new String[count];
		for (int i = 0; i < count; i++) {
			result[i] = src.substring(start[i], end[i]);
		}
		return result;
	}

	/**
	 * Splits a string in several parts (tokens) that are separated by single delimiter
	 * characters. Delimiter is always surrounded by two strings.
	 *
	 * @param src           source to examine
	 * @param delimiter     delimiter character
	 * @param multipleAsOne      multiple  delimiter as one 
	 *
	 * @return array of tokens
	 */
	public static String[] splitc(String src, char delimiter,boolean multipleAsOne) {
		if (src.length() == 0) {
			return new String[] { Strings.EMPTY };
		}
		char[] srcc = src.toCharArray();

		int maxparts = srcc.length + 1;
		int[] start = new int[maxparts];
		int[] end = new int[maxparts];

		int count = 0;

		start[0] = 0;
		int s = 0, e=0;
		if (srcc[0] == delimiter) { // string starts with delimiter
			end[0] = 0;
			count++;
			if(!multipleAsOne&&e<srcc.length-1&&srcc[e+1]==delimiter) {
				s=e+1;
			}else {
				s = CharUtils.findFirstDiff(srcc, 1, delimiter);
				if (s == -1) { // nothing after delimiters
					return new String[] { Strings.EMPTY, Strings.EMPTY };
				}
			}
			start[1] = s; // new start
		}
		while (true) {
			// find new end
			e = CharUtils.findFirstEqual(srcc, s, delimiter);
			if (e == -1) {
				end[count] = srcc.length;
				break;
			}
			end[count] = e;

			// find new start
			count++;
			if(!multipleAsOne&&e<srcc.length-1&&srcc[e+1]==delimiter) {
				s=e+1;
			}else {
				s = CharUtils.findFirstDiff(srcc, e, delimiter);
				if (s == -1) {
					start[count] = end[count] = srcc.length;
					break;
				}
			}
			start[count] = s;
		}
		count++;
		String[] result = new String[count];
		for (int i = 0; i < count; i++) {
			result[i] = src.substring(start[i], end[i]);
		}
		return result;
	}
	/**
	 * ",a,,b" -> ["","a","","b",""]
	 * @param src
	 * @param delimiter
	 * @return
	 */
	public static String[] splitc(String src,char delimiter) {
		return splitc(src, delimiter,false);
	}

	/**
	 * Converts all of the characters in the string to lower case, based on the
	 * portal instance's default locale.
	 *
	 * @param s the string to convert
	 * @return the string, converted to lower case, or <code>null</code> if the
	 *         string is <code>null</code>
	 */
	public static String toLowerCase(String s) {
		return toLowerCase(s, null);
	}

	/**
	 * Converts all of the characters in the string to lower case, based on the
	 * locale. More efficient than <code>String.toLowerCase</code>.
	 *
	 * @param s the string to convert
	 * @param locale apply this locale's rules, if <code>null</code> default
	 *        locale is used
	 * @return the string, converted to lower case, or <code>null</code> if the
	 *         string is <code>null</code>
	 */
	public static String toLowerCase(String s, Locale locale) {
		if (s == null) {
			return null;
		}
		StringBuilder sb = null;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);

			if (c > 127) {
				// found non-ascii char, fallback to the slow unicode detection
				if (locale == null) {
					locale = Locale.getDefault();
				}

				return s.toLowerCase(locale);
			}

			if ((c >= 'A') && (c <= 'Z')) {
				if (sb == null) {
					sb = new StringBuilder(s);
				}
				sb.setCharAt(i, (char) (c + 32));
			}
		}

		if (sb == null) {
			return s;
		}

		return sb.toString();
	}

	/**
	 * Converts all of the characters in the string to upper case, based on the
	 * portal instance's default locale.
	 *
	 * @param s the string to convert
	 * @return the string, converted to upper case, or <code>null</code> if the
	 *         string is <code>null</code>
	 */
	public static String toUpperCase(String s) {
		return toUpperCase(s, null);
	}

	/**
	 * Converts all of the characters in the string to upper case, based on the
	 * locale.
	 *
	 * @param s the string to convert
	 * @param locale apply this locale's rules
	 * @return the string, converted to upper case, or <code>null</code> if the
	 *         string is <code>null</code>
	 */
	public static String toUpperCase(String s, Locale locale) {
		if (s == null) {
			return null;
		}

		StringBuilder sb = null;

		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);

			if (c > 127) {

				if (locale == null) {
					locale = Locale.getDefault();
				}

				return s.toUpperCase(locale);
			}

			if ((c >= 'a') && (c <= 'z')) {
				if (sb == null) {
					sb = new StringBuilder(s);
				}

				sb.setCharAt(i, (char) (c - 32));
			}
		}

		if (sb == null) {
			return s;
		}

		return sb.toString();
	}

	/**
	 * 是否是以一个字符结束  作了null的判断 
	 * @param string 需验证的字符串
	 * @param end 结束的字符
	 * @return  为 null 时返回fase
	 */
	public static boolean endsWith(String string, String end) {
		if (isEmpty(string)) {
			return false;
		}
		return string.endsWith(end);
	}
	
	// endsWith
    //-----------------------------------------------------------------------

    /**
     * <p>Check if a CharSequence ends with a specified suffix.</p>
     *
     * <p>{@code null}s are handled without exceptions. Two {@code null}
     * references are considered to be equal. The comparison is case sensitive.</p>
     *
     * <pre>
     * StringUtils.endsWith(null, null)      = true
     * StringUtils.endsWith(null, "def")     = false
     * StringUtils.endsWith("abcdef", null)  = false
     * StringUtils.endsWith("abcdef", "def") = true
     * StringUtils.endsWith("ABCDEF", "def") = false
     * StringUtils.endsWith("ABCDEF", "cde") = false
     * </pre>
     *
     * @see java.lang.String#endsWith(String)
     * @param str  the CharSequence to check, may be null
     * @param suffix the suffix to find, may be null
     * @return {@code true} if the CharSequence ends with the suffix, case sensitive, or
     *  both {@code null}
     * @since 2.4
     * @since 3.0 Changed signature from endsWith(String, String) to endsWith(CharSequence, CharSequence)
     */
    public static boolean endsWith(final CharSequence str, final CharSequence suffix) {
        return endsWith(str, suffix, false);
    }

    /**
     * <p>Case insensitive check if a CharSequence ends with a specified suffix.</p>
     *
     * <p>{@code null}s are handled without exceptions. Two {@code null}
     * references are considered to be equal. The comparison is case insensitive.</p>
     *
     * <pre>
     * StringUtils.endsWithIgnoreCase(null, null)      = true
     * StringUtils.endsWithIgnoreCase(null, "def")     = false
     * StringUtils.endsWithIgnoreCase("abcdef", null)  = false
     * StringUtils.endsWithIgnoreCase("abcdef", "def") = true
     * StringUtils.endsWithIgnoreCase("ABCDEF", "def") = true
     * StringUtils.endsWithIgnoreCase("ABCDEF", "cde") = false
     * </pre>
     *
     * @see java.lang.String#endsWith(String)
     * @param str  the CharSequence to check, may be null
     * @param suffix the suffix to find, may be null
     * @return {@code true} if the CharSequence ends with the suffix, case insensitive, or
     *  both {@code null}
     * @since 2.4
     * @since 3.0 Changed signature from endsWithIgnoreCase(String, String) to endsWithIgnoreCase(CharSequence, CharSequence)
     */
    public static boolean endsWithIgnoreCase(final CharSequence str, final CharSequence suffix) {
        return endsWith(str, suffix, true);
    }

    /**
     * <p>Check if a CharSequence ends with a specified suffix (optionally case insensitive).</p>
     *
     * @see java.lang.String#endsWith(String)
     * @param str  the CharSequence to check, may be null
     * @param suffix the suffix to find, may be null
     * @param ignoreCase indicates whether the compare should ignore case
     *  (case insensitive) or not.
     * @return {@code true} if the CharSequence starts with the prefix or
     *  both {@code null}
     */
    private static boolean endsWith(final CharSequence str, final CharSequence suffix, final boolean ignoreCase) {
        if (str == null || suffix == null) {
            return str == null && suffix == null;
        }
        if (suffix.length() > str.length()) {
            return false;
        }
        final int strOffset = str.length() - suffix.length();
        return regionMatches(str, ignoreCase, strOffset, suffix, 0, suffix.length());
    }

	/**
	 * 配置匹配字符串转为正则表达式的字符串 
	 * 对匹配做了一个小调整
	 * 如：* 为匹配多个任意字符 . 只匹配点 增加字符串结尾 开始正则符
	 * 
	 * @param str
	 * @return
	 */
	public static String configString2Reg(String str) {
		String strs = trim(str).replaceAll("\\.", "\\\\\\.");
		strs = strs.replaceAll("\\*", ".*");
		return "^" + strs + "$";
	}
	/***
	 * 是否包含子串
	 * @param str
	 * @param sub
	 * @return
	 */
	public static boolean contains(String str,CharSequence sub){
		if(str==null||sub==null){
			return false;
		}
		return str.contains(sub);
	}
	/**
	 * 是否存在子串
	 * @param str
	 * @param sub
	 * @return
	 */
	public static boolean hasSubstr(String str,CharSequence sub) {
		if(str==null||sub==null){
			return false;
		}
		return str.contains(sub);
	}
	/**
	 * 	是否包含字符
	 * @param str
	 * @param c
	 * @return
	 */
	public static boolean contains(String str,char c) {
		if(str==null){
			return false;
		}
		return str.indexOf(c)>=0;
	}
	/**
     * <p>Checks if the CharSequence contains any character in the given
     * 	包含其中任何一个字符
     * set of characters.</p>
     */
	public static boolean containsAny(CharSequence cs, char... searchChars) {
		if ((isEmpty(cs)) || (ArrayUtils.isEmpty(searchChars))) {
			return false;
		}
		int csLength = cs.length();
		int searchLength = searchChars.length;
		int csLast = csLength - 1;
		int searchLast = searchLength - 1;
		for (int i = 0; i < csLength; i++) {
			char ch = cs.charAt(i);
			for (int j = 0; j < searchLength; j++) {
				if (searchChars[j] == ch) {
					if (Character.isHighSurrogate(ch)) {
						if (j == searchLast) {
							return true;
						}
						if ((i < csLast) && (searchChars[(j + 1)] == cs.charAt(i + 1))) {
							return true;
						}
					} else {
						return true;
					}
				}
			}
		}
		return false;
	}
	/**
	 * 两个字符串连接 为null里转为空字符串
	 * @param str
	 * @param str1
	 * @return
	 */
	public static String concat(String str,String str1) {
		if(str==null) {
			return str1==null?"":str1;
		}
		return str1==null?"":str1.concat(str1);
	}
	
	/**
	 * 连接两个字符串 发果是null当成空字符串
	 * @param str
	 * @param str2
	 * @return
	 */
	public static String concat(String str,String str1,String... str2){
		StringBuilder resStr=new StringBuilder();
		if(str!=null){
			resStr.append(str);
		}
		if(str1!=null) {
			resStr.append(str1);
		}
		if(str2!=null) {
			for(int i=0;i<str2.length;i++) {
				if(str2[i]!=null) {
					resStr.append(str2[i]);
				}
			}
		}
		return resStr.toString();
	}

	/***
	 * 替换占位符 {index}
	 * 
	 * 如 ： 我是{0},你好  ["jim"]  --> 我是jim,你好
	 * 
	 * @param template 要替换占位符的字符串
	 * @param args 参数位参数的值
	 * @return 占位替换后的字符串
	 */
	public static String replaceFlag(String template, final Object[] args) {
		if (ArrayUtils.notEmpty(args)) {
			return replaceParer.parse(template, new MacroResolver() {
				@Override
				public String resolve(String macroName) {
					int index=Integer.valueOf(macroName);
					return String.valueOf(args[index]);
				}
			});
		}
		return template;
	}

	/***
	 * 替换占位符 {index}
	 * 
	 * @param template
	 * @param args
	 * @return
	 */
	public static String replaceFlag(String template, final Object first, final Object... args) {
		return replaceParer.parse(template, new MacroResolver() {
			@Override
			public String resolve(String macroName) {
				int index=Integer.valueOf(macroName);
				if(index==0){
					return String.valueOf(first);
				}
				return String.valueOf(args[index-1]);
			}
		});
	}
	/**
	 * 参数数组索引替换方式格式化
	 * {0}你好{1} hello,world  --> hello你好world
	 * @param template
	 * @param first
	 * @param args
	 * @return
	 */
	public static String msgFmt(String template,final Object first,final Object... args) {
		return replaceFlag(template, first, args);
	}
	/**
	 * 	使用模板方式格式化一个字符串
	 * ${name}你好
	 * @param template
	 * @param params
	 * @return
	 */
	public static String templateFmt(String template,Object params) {
		return templateParer.parse(template, new BaseMacroResolver(params));
	}

	/**
	 * 替换所有字符  参考  {@link String}.replaceAll(regex, replace)
	 * @param str  源字符串
	 * @param regex 替换表达式
	 * @param replace 替换成
	 * @return
	 */
	public static String replaceAll(String str, String regex, String replace) {
		if (notEmpty(str)) {
			str = str.replaceAll(regex, replace);
		}
		return str;
	}
	
	/**
	 * 替换字符串
	 * @param source
	 * @param str
	 * @param replace
	 * @return
	 */
	public static String replace(String source,String str,String replace) {
		if(isEmpty(source)) {
			return source;
		}
		return source.replaceAll(str, replace);
	}

	/***
	 * 转成十六进制
	 * @param digest
	 * @return
	 */
	public static String toHexString(byte[] digest) {
		char[] chars = new char[digest.length * 2];
		int i = 0;
		for (byte b : digest) {
			chars[i++] = CharUtils.int2HEX((b & 0xF0) >> 4);
			chars[i++] = CharUtils.int2HEX(b & 0x0F);
		}
		return new SharedString(chars).toString();
	}
	/**
	 * 转成小写十六进制
	 * @param digest
	 * @return
	 */
	public static String toHexLowerString(byte[] digest) {
		char[] chars = new char[digest.length * 2];
		int i = 0;
		for (byte b : digest) {
			chars[i++] = CharUtils.int2hex((b & 0xF0) >> 4);
			chars[i++] = CharUtils.int2hex(b & 0x0F);
		}
		return new SharedString(chars).toString();
	}

	/**
	 * Returns the very first index of any char from provided string, starting from specified index offset.
	 * Returns index of founded char, or <code>-1</code> if nothing found.
	 */
	public static int indexOfAnyChar(CharSequence string, char[] chars, int startindex) {
		int stringLen = string.length();
		int charsLen = chars.length;
		for (int i = startindex; i < stringLen; i++) {
			char c = string.charAt(i);
			for (int j = 0; j < charsLen; j++) {
				if (c == chars[j]) {
					return i;
				}
			}
		}
		return -1;
	}
	/**
	 * 判断一个字符在字符串中的索引值
	 * @param string
	 * @param chr
	 * @param startIndex 从此索引开始检索
	 * @return
	 */
	public static int indexOf(CharSequence string,char chr,int startIndex){
		int stringLen = string.length();
		for (int i = startIndex; i < stringLen; i++) {
			char c = string.charAt(i);
			if (c == chr) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * 判断一个字符在字符串中的索引值 从0 开始
	 * @param string
	 * @param chr
	 * @return
	 */
	public static int indexOf(CharSequence string,char chr){
		return indexOf(string, chr,0);
	}

	/**
	 * 返回任意一个指定字符出现的索引
	 * @param string
	 * @param chars
	 * @return
	 */
	public static int indexOfAnyChar(CharSequence string, char[] chars) {
		return indexOfAnyChar(string, chars, 0);
	}

	/**
	 * 给定一个分隔前的字串
	 * @param str
	 * @param separator
	 * @return
	 */
	public static String substringBefore(String str, String separator) {
		return substringBefore(str, separator, 0);
	}
	
	public static String substringBefore(String str,String separator,int fromIndex){
		if (isEmpty(str)) {
			return str;
		}
		if (separator == null) {
			return Strings.BLANK;
		}
		int pos = str.indexOf(separator,fromIndex);
		if (pos == -1) {
			return Strings.BLANK;
		}
		return str.substring(fromIndex,pos);
	}

	/**
	 * 最后一个分隔符之前的子串
	 * @param str
	 * @param separator
	 * @return
	 */
	public static String substringBeforeLast(String str, String separator) {
		if (isEmpty(str)) {
			return str;
		}
		if(isEmpty(separator)){
			return Strings.BLANK;
		}
		int pos = str.lastIndexOf(separator);
		if (pos == -1) {
			return Strings.BLANK;
		}
		return str.substring(0, pos);
	}
	/**
	 * 在一个子串之后的内容
	 * @param str
	 * @param separator
	 * @return
	 */
	public static String substringAfter(String str,String separator) {
		return substringAfter(str, separator,0);
	}

	/**
	 * 给定一个分隔后的字串
	 * @param str
	 * @param separator
	 * @return
	 */
	public static String substringAfter(String str, String separator,int fromIndex) {
		if (isEmpty(str)) {
			return str;
		}
		if (isEmpty(separator)) {
			return Strings.BLANK;
		}
		int pos = str.indexOf(separator,fromIndex);
		if (pos == -1) {
			return Strings.BLANK;
		}
		return str.substring(pos + separator.length());
	}
	/**
	 * 最后一个分隔符之后的子串
	 * @param str
	 * @param separator
	 * @return
	 */
	public static String substringAfterLast(String str, String separator,int fromIndex) {
		if (isEmpty(str)) {
			return str;
		}
		if (isEmpty(separator)) {
			return Strings.BLANK;
		}
		int pos = str.lastIndexOf(separator,fromIndex);
		if ((pos == -1) || (pos > fromIndex - separator.length())) {
			return Strings.BLANK;
		}
		return str.substring(pos + separator.length());
	}
	
	/**
	 * 在最后一个子串之后的内容
	 * @param str
	 * @param separator
	 * @return
	 */
	public static String substringAfterLast(String str, String separator) {
		return substringAfterLast(str, separator, str.length());
	}

	/**
	 * @param str
	 * @param tag 两个相同的分隔符
	 * @return
	 */
	public static String substringBetween(String str, String tag) {
		return substringBetween(str, tag, tag);
	}
	/**
	 * 字符串间的子串
	 * @param str
	 * @param tag
	 * @return
	 */
	public static String substringBetween(String str,char tag){
		return substringBetween(str, tag,tag);
	}
	
	/**
	 * 字符串第一个和最后一个间的子串
	 * @param str
	 * @param first 起始标签
	 * @param last 结束标签
	 * @return
	 */
	public static String substringBetweenFirstAndLast(String str,String first,String last){
		if ((str == null) || (first == null) || (last == null)) {
			return null;
		}
		int start = str.indexOf(first);
		if (start != -1) {
			int end = str.lastIndexOf(last);
			start += first.length();
			if (end != -1&&end>start) {
				return str.substring(start, end);
			}
		}
		return null;
	}
	/**
	 * 字符串第一个和最后一个间的子串
	 * @param str
	 * @param tag 标记
	 * @return
	 */
	public static String substringBetweenFirstAndLast(String str,String tag){
		return substringBetweenFirstAndLast(str, tag, tag);
	}
	/**
	 * 两个字符之间的子串
	 * @param str
	 * @param open
	 * @param close
	 * @return
	 */
	public static String substringBetween(String str,char open,char close){
		return substringBetween(str, open,close,0);
	}
	/**
	 * 在两个字符中间的子串
	 * @param str
	 * @param open
	 * @param close
	 * @param first
	 * @return
	 */
	public static String substringBetween(String str,char open,char close,int first){
		if ((str == null)) {
			return null;
		}
		int start = str.indexOf(open,first);
		if (start != -1) {
			int end = str.indexOf(close, start + 1);
			if (end != -1) {
				return str.substring(start + 1, end);
			}
		}
		return null;
	}

	/**
	 * 两个分隔符中间的子字符串
	 * @param str 源目标字符串
	 * @param open 开始分隔
	 * @param close 结束分隔符
	 * @return
	 */
	public static String substringBetween(String str, String open, String close) {
		return substringBetween(str, open, close,0);
	}
	/**
	 * 两个分隔符中间的子字符串
	 * @param str 源目标字符串
	 * @param open 开始分隔
	 * @param close 结束分隔符
	 * @param index 起始查询索引
	 * @return
	 */
	public static String substringBetween(String str, String open, String close,int index) {
		if ((str == null) || (open == null) || (close == null)) {
			return null;
		}
		int start = str.indexOf(open,index);
		if (start != -1) {
			int end = str.indexOf(close, start + open.length());
			if (end != -1) {
				return str.substring(start + open.length(), end);
			}
		}
		return null;
	}

	/**
	 * 子字符串在源字符串中的位置
	 * @param src 源字符串
	 * @param sub 子字符串
	 * @param startIndex 从此索引开始查找
	 * @param endIndex 到此索引结束
	 * @return
	 */
	public static int indexOf(CharSequence src, CharSequence sub, int startIndex, int endIndex) {
		if (startIndex < 0) {
			startIndex = 0;
		}
		int srclen = src.length();
		if (endIndex > srclen) {
			endIndex = srclen;
		}
		int sublen = sub.length();
		if (sublen == 0) {
			return startIndex > srclen ? srclen : startIndex;
		}

		int total = endIndex - sublen + 1;
		char c = sub.charAt(0);
		mainloop: 
		for (int i = startIndex; i < total; i++) {
			if (src.charAt(i) != c) {
				continue;
			}
			int j = 1;
			int k = i + 1;
			while (j < sublen) {
				if (sub.charAt(j) != src.charAt(k)) {
					continue mainloop;
				}
				j++;
				k++;
			}
			return i;
		}
		return -1;
	}
	
	/**
	 * 子字符串在源字符串中的位置
	 * @param src 源字符串
	 * @param sub 子字符串
	 * @param startIndex 从此索引开始查找
	 * @return
	 */
	public static int indexOf(CharSequence src, CharSequence sub, int startIndex) {
		return indexOf(src, sub, startIndex, src.length());
	}
	/**
	 * 子字符串在源字符串中的位置
	 * @param src 源字符串
	 * @param sub 子字符串
	 * @return
	 */
	public static int indexOf(CharSequence src, CharSequence sub) {
		return indexOf(src, sub, 0, src.length());
	}

	/**
	 * 对String substring方法进行索引异常兼容
	 *  如果源为 null 时返回 null
	 *  为空字符串时 返回空字符串
	 * @param source  
	 * @param start 起始索引
	 * @param end 线束索引
	 * @return 字字符串  
	 */
	public static String substring(String source, int start, int end) {
		if (source == null) {
			return null;
		} else if (Strings.BLANK.equals(source)) {
			return Strings.BLANK;
		} else {
			if (end > source.length()) {
				end = source.length();
			}
			return source.substring(start, end);
		}
	}

	/**
	 * 从起始索引截取一个长度的子串
	 * 如没有足够的长度，到字符串结束为止
	 * @param str
	 * @param start 起始索引
	 * @param length 长度
	 * @return
	 */
	public static String substr(String str, int start, int length) {
		if (isEmpty(str)) {
			return str;
		} else {
			int end = start + length;
			if (end > str.length()) {
				end = str.length();
			}
			return str.substring(start, end);
		}
	}

	/**
	 * 是否都是小写字符
	 * @param cs
	 * @return
	 */
	public static boolean isAllLowerCase(CharSequence cs) {
		if ((cs == null) || (isEmpty(cs))) {
			return false;
		}
		int sz = cs.length();
		for (int i = 0; i < sz; i++) {
			if (!Character.isLowerCase(cs.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 是否都是大写字符
	 * @param cs
	 * @return
	 */
	public static boolean isAllUpperCase(CharSequence cs) {
		if (isEmpty(cs)) {
			return false;
		}
		int sz = cs.length();
		for (int i = 0; i < sz; i++) {
			if (!Character.isUpperCase(cs.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 转大写字符
	 * @param str
	 * @return
	 */
	public static String upperCase(String str) {
		if (str == null) {
			return null;
		}
		return str.toUpperCase();
	}

	/**
	 * 参考String eqauls 方法
	 * @param str1 
	 * @param str2
	 * @param isNullTrue 两个都为null时是否返回true
	 * @return
	 */
	public static boolean equals(String str1, String str2, boolean isNullTrue) {
		if (str1 == null && str2 == null) {
			return isNullTrue;
		}
		if (str1 == null) {
			return false;
		}
		return str1.equals(str2);
	}

	/**
	 * 如果两个都是null返回false
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static boolean equals(String str1, String str2) {
		return equals(str1, str2, true);
	}
	/**
	 * 非equals 
	 * @param str1
	 * @param str2
	 * @param isNullTrue 
	 * @return
	 */
	public static boolean notEquals(String str1,String str2,boolean isNullTrue){
		if (str1 == null && str2 == null) {
			return isNullTrue;
		}
		if (str1 == null) {
			return true;
		}
		return !str1.equals(str2);
	}
	/**
	 * 两都都是null时返回true
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static boolean notEquals(String str1,String str2){
		return notEquals(str1, str2, true);
	}

	/**
	 * 参考String equalsIgnoreCase 方法
	 * @param str1 
	 * @param str2
	 * @param isNullTrue 两个都为null时是否返回true
	 * @return
	 */
	public static boolean equalsIgnoreCase(String str1, String str2, boolean isNullTrue) {
		if (str1 == null && str2 == null) {
			return isNullTrue;
		}
		if (str1 == null) {
			return false;
		}
		return str1.equalsIgnoreCase(str2);
	}

	/**
	 * 两个都为null时返回false
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static boolean equalsIgnoreCase(String str1, String str2) {
		return equalsIgnoreCase(str1, str2, true);
	}

	/**
	 * 除引号字符串
	 * @param obj
	 * @return
	 */
	public static String valueOfQuoted(String obj) {
		return obj.substring(1, obj.length() - 1);
	}

	/***
	 * 无引号字符串 把“null” 转null 
	 * @param obj
	 * @return
	 */
	public static String valueOfNoQuoted(String obj) {
		if (Strings.NULL.equals(obj)) {
			return null;
		} else {
			return obj;
		}
	}

	/**
	 * Returns if string ends with provided character.
	 */
	public static boolean endsWith(String s, char c) {
		if (s.length() == 0) {
			return false;
		}
		return s.charAt(s.length() - 1) == c;
	}
	/**
	 * 以字符起始
	 * @param s
	 * @param c
	 * @return
	 */
	public static boolean startsWith(String s, char c) {
		if (s.length() == 0) {
			return false;
		}
		return s.charAt(0) == c;
	}
	
	
	/**
	 * 以字符串起始
	 * @param s
	 * @param c
	 * @return
	 */
	public static boolean startsWith(String s, String c) {
		if (isEmpty(s)) {
			return false;
		}
		return s.startsWith(c);
	}
	/**
	 *      删除一个字符串中的字符
	 * remove("123456789",['2','5'])-->"1346789"
	 * @param column
	 * @param remove 要删除的所有字符的集合
	 * @return
	 */
	public static String remove(String column,char[] remove){
		int len=column.length();
		char[] array=column.toCharArray();
		int srcBegin=0;
		int dstBegin=0;
		int removeCount=0;
		char c;
		int j;
		for(int i=0;i<len;i++){
			c=array[i];
			for(j=0;j<remove.length;j++){
				if(remove[j]==c){
					System.arraycopy(array, srcBegin, array, dstBegin, i-srcBegin);
					dstBegin=i-removeCount;
					removeCount++;
					srcBegin=i+1;
					break;
				}
			}
		}
		System.arraycopy(array, srcBegin, array, dstBegin, len-srcBegin);
		return new String(array, 0, len-removeCount);
	}
	/**
	 * 是否为非空、非空格串
	 * @param cs
	 * @return
	 */
	public static boolean hasText(CharSequence cs){
		if (isEmpty(cs)) {
			return false;
		}
		int strLen = cs.length();
		for (int i = 0; i < strLen; i++) {
			if (!Character.isWhitespace(cs.charAt(i))) {
				return true;
			}
		}
		return false;
	}
	/**
	 * 为空 或 空格时抛出异常
	 * @param cs
	 * @param exception
	 */
	public static void noText(CharSequence cs,String exception){
		if (!StringUtils.hasText(cs)) {
			throw new IllegalArgumentException(exception);
		}
	}
	
	/***
	 * 以一个长度拆分
	 * @param 
	 * @param len 以一个长度拆分
	 * @return
	 */
	public static List<String> splitByLen(String sourceStr,int len){
		List<String> snList=new LinkedList<String>();
		char[] value=sourceStr.toCharArray();
		for(int i=0;i<value.length;i+=len){
			int end=i+len;
			if(end>value.length){
				end=value.length;
			}
			char[] arrayOfChar = new char[end-i];
		    System.arraycopy(value, i, arrayOfChar, 0, arrayOfChar.length);
			snList.add(new SharedString(arrayOfChar).toString());
		}
		return snList;
	}
	/**
	 * 把一个数组进行连接  
	 * join([1,2,3],",",0,2) --> 1,2
	 * @param a
	 * @param split
	 * @param startIndex 
	 * @param endIndex
	 * @return
	 */
	public static String join(int[] a, String split,int startIndex,int endIndex) {
		int len=endIndex-startIndex;
		if(len<=0){
			return Strings.BLANK;
		}
		StringBuilder strs=new StringBuilder(len*8);
		for(int i=startIndex;i<endIndex;i++){
			strs.append(split).append(a[i]);
		}
		return strs.substring(split.length());
	}
	/**
	 * 连接一个数组 
	 * join({1,2,3},",") --> 1,2,3
	 * @param a
	 * @param split
	 * @return
	 */
	public static String join(int[] a,String split){
		if(split==null){
			split=Strings.BLANK;
		}
		return join(a, split,0,a.length);
	}
	
	/**
	 *	连接一个数组
	 * @param a 
	 * @param split 分隔符
	 * @param startIndex 数组要连接的超始索引
	 * @param endIndex 数组结束的索引(不包含)
	 * @return
	 */
	public static String join(long[] a, String split,int startIndex,int endIndex) {
		int len=endIndex-startIndex;
		if(len<=0){
			return Strings.BLANK;
		}
		StringBuilder strs=new StringBuilder(len*16);
		for(int i=startIndex;i<endIndex;i++){
			strs.append(split).append(a[i]);
		}
		return strs.substring(split.length());
	}
	
	public static String join(long[] a,String split){
		if(split==null){
			split=Strings.BLANK;
		}
		return join(a, split,0,a.length);
	}
	/**
	 * 连接成一个字符串
	 * @param a 数组
	 * @param split 分隔符
	 * @param startIndex 
	 * @param endIndex
	 * @return
	 */
	public static String join(short[] a, String split,int startIndex,int endIndex) {
		int len=endIndex-startIndex;
		if(len<=0){
			return Strings.BLANK;
		}
		StringBuilder strs=new StringBuilder(len*4);
		for(int i=startIndex;i<endIndex;i++){
			strs.append(split).append(a[i]);
		}
		return strs.substring(split.length());
	}
	/**
	 * 连接成一个字符串 
	 * @param a
	 * @param split
	 * @return
	 */
	public static String join(short[] a,String split){
		if(split==null){
			split=Strings.BLANK;
		}
		return join(a, split,0,a.length);
	}
	/**
	 * 是否是整数
	 * isDigit("")  false
	 * @param str
	 * @return
	 */
   public static boolean isDigits(String str) {
       if (isEmpty(str)) {
           return false;
       }
       int sz = str.length();
       for (int i = 0; i < sz; i++) {
           if (Character.isDigit(str.charAt(i)) == false) {
               return false;
           }
       }
       return true;
   }
   /**
    * 字符串长度
    * @param str
    * @return
    */
   public static int length(CharSequence str){
	   if(str==null){
		   return 0;
	   }
	   return str.length();
   }
   /**
    * <p>
    * Wraps a string with a char.
    * </p>
    * 
    * <pre>
    * StringUtils.wrap(null, *)        = null
    * StringUtils.wrap("", *)          = ""
    * StringUtils.wrap("ab", '\0')     = "ab"
    * StringUtils.wrap("ab", 'x')      = "xabx"
    * StringUtils.wrap("ab", '\'')     = "'ab'"
    * StringUtils.wrap("\"ab\"", '\"') = "\"\"ab\"\""
    * </pre>
    * 
    * @param str
    *            the string to be wrapped, may be {@code null}
    * @param wrapWith
    *            the char that will wrap {@code str}
    * @return the wrapped string, or {@code null} if {@code str==null}
    * @since 3.4
    */
   public static String wrap(final String str, final char wrapWith) {

       if (isEmpty(str) || wrapWith == '\0') {
           return str;
       }

       return wrapWith + str + wrapWith;
   }
   /**
    * <p>
    * Wraps a String with another String.
    * </p>
    * 
    * <p>
    * A {@code null} input String returns {@code null}.
    * </p>
    * 
    * <pre>
    * StringUtils.wrap(null, *)         = null
    * StringUtils.wrap("", *)           = ""
    * StringUtils.wrap("ab", null)      = "ab"
    * StringUtils.wrap("ab", "x")       = "xabx"
    * StringUtils.wrap("ab", "\"")      = "\"ab\""
    * StringUtils.wrap("\"ab\"", "\"")  = "\"\"ab\"\""
    * StringUtils.wrap("ab", "'")       = "'ab'"
    * StringUtils.wrap("'abcd'", "'")   = "''abcd''"
    * StringUtils.wrap("\"abcd\"", "'") = "'\"abcd\"'"
    * StringUtils.wrap("'abcd'", "\"")  = "\"'abcd'\""
    * </pre>
    * 
    * @param str
    *            the String to be wrapper, may be null
    * @param wrapWith
    *            the String that will wrap str
    * @return wrapped String, {@code null} if null String input
    * @since 3.4
    */
   public static String wrap(final String str, final String wrapWith) {

       if (isEmpty(str) || isEmpty(wrapWith)) {
           return str;
       }

       return wrapWith.concat(str).concat(wrapWith);
   }
   
   /**
    * Green implementation of toCharArray.
    *
    * @param cs the {@code CharSequence} to be processed
    * @return the resulting char array
    */
   public static char[] toCharArray(final CharSequence cs) {
       if (cs instanceof String) {
           return ((String) cs).toCharArray();
       }
       final int sz = cs.length();
       final char[] array = new char[cs.length()];
       for (int i = 0; i < sz; i++) {
           array[i] = cs.charAt(i);
       }
       return array;
   }
   
   /**
    * Green implementation of regionMatches.
    *
    * @param cs the {@code CharSequence} to be processed
    * @param ignoreCase whether or not to be case insensitive
    * @param thisStart the index to start on the {@code cs} CharSequence
    * @param substring the {@code CharSequence} to be looked for
    * @param start the index to start on the {@code substring} CharSequence
    * @param length character length of the region
    * @return whether the region matched
    */
   public static boolean regionMatches(final CharSequence cs, final boolean ignoreCase, final int thisStart,
           final CharSequence substring, final int start, final int length)    {
       if (cs instanceof String && substring instanceof String) {
           return ((String) cs).regionMatches(ignoreCase, thisStart, (String) substring, start, length);
       }
       int index1 = thisStart;
       int index2 = start;
       int tmpLen = length;

       while (tmpLen-- > 0) {
           final char c1 = cs.charAt(index1++);
           final char c2 = substring.charAt(index2++);

           if (c1 == c2) {
               continue;
           }

           if (!ignoreCase) {
               return false;
           }

           // The same check as in String.regionMatches():
           if (Character.toUpperCase(c1) != Character.toUpperCase(c2)
                   && Character.toLowerCase(c1) != Character.toLowerCase(c2)) {
               return false;
           }
       }

       return true;
   }
   
   /**
    * <p>Repeat a String {@code repeat} times to form a
    * new String.</p>
    *
    * <pre>
    * StringUtils.repeat(null, 2) = null
    * StringUtils.repeat("", 0)   = ""
    * StringUtils.repeat("", 2)   = ""
    * StringUtils.repeat("a", 3)  = "aaa"
    * StringUtils.repeat("ab", 2) = "abab"
    * StringUtils.repeat("a", -2) = ""
    * </pre>
    *
    * @param str  the String to repeat, may be null
    * @param repeat  number of times to repeat str, negative treated as zero
    * @return a new String consisting of the original String repeated,
    *  {@code null} if null String input
    */
   public static String repeat(final String str, final int repeat) {
       // Performance tuned for 2.0 (JDK1.4)

       if (str == null) {
           return null;
       }
       if (repeat <= 0) {
           return Strings.EMPTY;
       }
       final int inputLength = str.length();
       if (repeat == 1 || inputLength == 0) {
           return str;
       }
       if (inputLength == 1 && repeat <= PAD_LIMIT) {
           return repeat(str.charAt(0), repeat);
       }

       final int outputLength = inputLength * repeat;
       switch (inputLength) {
           case 1 :
               return repeat(str.charAt(0), repeat);
           case 2 :
               final char ch0 = str.charAt(0);
               final char ch1 = str.charAt(1);
               final char[] output2 = new char[outputLength];
               for (int i = repeat * 2 - 2; i >= 0; i--, i--) {
                   output2[i] = ch0;
                   output2[i + 1] = ch1;
               }
               return new String(output2);
           default :
               final StringBuilder buf = new StringBuilder(outputLength);
               for (int i = 0; i < repeat; i++) {
                   buf.append(str);
               }
               return buf.toString();
       }
   }

   /**
    * <p>Repeat a String {@code repeat} times to form a
    * new String, with a String separator injected each time. </p>
    *
    * <pre>
    * StringUtils.repeat(null, null, 2) = null
    * StringUtils.repeat(null, "x", 2)  = null
    * StringUtils.repeat("", null, 0)   = ""
    * StringUtils.repeat("", "", 2)     = ""
    * StringUtils.repeat("", "x", 3)    = "xxx"
    * StringUtils.repeat("?", ", ", 3)  = "?, ?, ?"
    * </pre>
    *
    * @param str        the String to repeat, may be null
    * @param separator  the String to inject, may be null
    * @param repeat     number of times to repeat str, negative treated as zero
    * @return a new String consisting of the original String repeated,
    *  {@code null} if null String input
    * @since 2.5
    */
   public static String repeat(final String str, final String separator, final int repeat) {
       if(str == null || separator == null) {
           return repeat(str, repeat);
       }
       // given that repeat(String, int) is quite optimized, better to rely on it than try and splice this into it
       int len=(str.length()+separator.length())*repeat;
       StringBuilder resSb=new StringBuilder(len);
       for(int i=0;i<repeat;i++) {
    	   resSb.append(str).append(separator);
       }
       resSb.setLength(resSb.length()-separator.length());
       return resSb.toString();
   }

   /**
    * <p>Returns padding using the specified delimiter repeated
    * to a given length.</p>
    *
    * <pre>
    * StringUtils.repeat('e', 0)  = ""
    * StringUtils.repeat('e', 3)  = "eee"
    * StringUtils.repeat('e', -2) = ""
    * </pre>
    *
    * <p>Note: this method doesn't not support padding with
    * <a href="http://www.unicode.org/glossary/#supplementary_character">Unicode Supplementary Characters</a>
    * as they require a pair of {@code char}s to be represented.
    * If you are needing to support full I18N of your applications
    * consider using {@link #repeat(String, int)} instead.
    * </p>
    *
    * @param ch  character to repeat
    * @param repeat  number of times to repeat char, negative treated as zero
    * @return String with repeated character
    * @see #repeat(String, int)
    */
   public static String repeat(final char ch, final int repeat) {
       final char[] buf = new char[repeat];
       for (int i = repeat - 1; i >= 0; i--) {
           buf[i] = ch;
       }
       return new String(buf);
   }
}
