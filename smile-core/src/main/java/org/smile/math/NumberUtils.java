package org.smile.math;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.smile.collection.ThreadLocalMap;
import org.smile.util.StringUtils;

/**
 * 数字操作工具类
 * 
 * @author strive
 * 
 */
public class NumberUtils {
	/** 82进制*/
	private static final char[] CHAR = {'0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9','a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
			'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
			'x', 'y', 'z','A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
			'K', 'L', 'M','N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
			'X', 'Y', 'Z', '*', '-', '_','+','/','=', '~', '@', '#', '(', ')', ',',
			'.',  '?', ':', ';', '<', '>', '$', '%', '^', '&'};
	
	// 字符与10进制数字的对应表
	private static final Map<Character, Integer> CHAR_MAP;
	/**罗马数据对应*/
	private static final int[] VALUES = new int[] {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
	/**罗马数据对应*/
	private static final String[] LETTERS = new String[] {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};

	protected static final String[] CHINESE_NUMBER={"零","一","二","三","四","五","六","七","八","九","十","十一","十二"
	,"十三","十四","十五","十六","十七","十八","十九","二十","二十一","二十二"
	,"二十三","二十四","二十五","二十六","二十七","二十八","二十九","三十","三十一","三十二"};
	
	public static final Format nf=new DecimalFormat("#0.##");
	/**可保留三位小数*/
	public static final Format nf_3=new DecimalFormat("#0.###");
	
	private static final Map<String,Format> numberFormat=new ThreadLocalMap<String, Format>();
	
	
	static {
		CHAR_MAP = new HashMap<Character, Integer>();
		for (int i = 0; i < CHAR.length; i++) {
			CHAR_MAP.put(CHAR[i], i);
		}
		
	}
	
	public static Format getFormat(String text){
		Format format=numberFormat.get(text);
		if(format==null){
			format= new DecimalFormat(text);
			numberFormat.put(text,format);
		}
		return format;
	}

	/**
	 * 
	 * @param i
	 * @param shift 进制左移位数 1 为二进制 2 为四进制 3为八进制
	 * @return
	 */
	public static String toUnsignedString(int i, int shift) {
        char[] buf = new char[32];
        int charPos = 32;
        int radix = 1 << shift;
        int mask = radix - 1;
        do {
            buf[--charPos] = CHAR[i & mask];
            i >>>= shift;
        } while (i != 0);

        return new String(buf, charPos, (32 - charPos));
    }
	
	/**
	 * 转换十进制为其它进制字符串
	 * 
	 * @param i
	 * @param radix
	 * @return
	 */
	public static String toString(long i, int radix) {
		if (radix < Character.MIN_RADIX || radix > CHAR.length) {
			radix = 10;
		}
		if (radix == 10){
			return String.valueOf(i);
		}
		char[] buf = new char[65];
		int charPos = 64;
		boolean negative = (i < 0);

		if (!negative) {
			i = -i;
		}

		while (i <= -radix) {
			buf[charPos--] = CHAR[(int) (-(i % radix))];
			i = i / radix;
		}
		buf[charPos] = CHAR[(int) (-i)];

		if (negative) {
			buf[--charPos] = '-';
		}
		return new String(buf, charPos, (65 - charPos));
	}

	/**
	 * 数字格式化
	 * @param number
	 * @param formatText
	 * @return
	 */
	public static String format(Object number, String formatText) {
		DecimalFormat df = new DecimalFormat(formatText);
		return df.format(number);
	}

	/**
	 * 字符串解析成Long
	 * @param number
	 * @param radix
	 * @return
	 */
	public static Long parseLong(String number, int radix) {
		if(number.charAt(0)=='-'){
			return -parseLong(number.substring(1,number.length()),radix);
		}
		if (radix > CHAR.length) {
			throw new RuntimeException(new StringBuffer("指定的进制").append(radix)
					.append("太大不支持解析").toString());
		}
		long result = 0;
		char[] numberChar = number.toCharArray();
		char currentChar;
		int bit;
		int len = numberChar.length;
		for (int i = len - 1; i >= 0; i--) {
			currentChar = numberChar[len - i - 1];
			bit = CHAR_MAP.get(currentChar);
			if (bit > radix) {
				throw new RuntimeException(new StringBuffer("解析数字出错:字符 ")
						.append(currentChar).append(" 代表数字").append(bit)
						.append("大于进制值").append(radix).toString());
			}
			result += bit * Math.pow((double) radix, i);
		}
		return result;
	}
	/**
	 * 解析成int型
	 * @param number
	 * @return
	 */
	public static int parseInt(Object number){
		if(number==null){
			return 0;
		}else if(number instanceof Number){
			return ((Number) number).intValue();
		}else{
			return Integer.parseInt(number.toString());
		}
	}
	/**
	 * 转成长整型
	 * @param number
	 * @return
	 */
	public static long parseLong(Object number){
		if(number==null){
			return 0;
		}else if(number instanceof Number){
			return ((Number) number).longValue();
		}else{
			return Long.parseLong(number.toString());
		}
	}
	/**
	 * 转换成BigDecimal
	 * @param number 数字
	 * @return
	 */
	public static BigDecimal parseDecimal(Object number){
		if(StringUtils.isNull(number)){
			return new BigDecimal(0);
		}else if(number instanceof Number){
			return new BigDecimal(((Number)number).toString());
		}
		return new BigDecimal(number.toString());
	}
	
	/**
	 * 解析成double类型
	 * @param number
	 * @return
	 */
	public static double parseDouble(Object number){
		if(StringUtils.isNull(number)){
			return 0;
		}else if(number instanceof Number){
			return ((Number)number).doubleValue();
		}
		return Double.valueOf(number.toString());
	}
	/**
	 * 解析成double型  如是空返回 0
	 * @param number
	 * @return
	 */
	public static double parseDouble(String number){
		if(StringUtils.isEmpty(number)){
			return 0;
		}else{
			return Double.valueOf(number);
		}
	}
	/**
	 * 默认的格式化样式
	 * @param number
	 * @return
	 */
	public static String format(Number number){
		return nf.format(number);
	}
	/**
	 * 格式化数字
	 * @param number
	 * @param text  格式
	 * @return
	 */
	public static String format(Number number,String text){
		Format format=getFormat(text);
		return format.format(number);
	}
	
	public static char getChar(int index){
		return CHAR[index];
	}
	/**
	 * 中文数字
	 * @param index
	 * @return
	 */
	public static String getChinese(int index){
		return CHINESE_NUMBER[index];
	}
	
	/**
	 * 阿拉伯转罗马
	 * Converts to Roman number.
	 */
	public static String convertToRoman(int value) {
		if (value <= 0) {
			throw new IllegalArgumentException();
		}
		StringBuilder roman = new StringBuilder();
		int n = value;
		for (int i = 0; i < LETTERS.length; i++) {
			while (n >= VALUES[i]) {
				roman.append(LETTERS[i]);
				n -= VALUES[i];
			}
		}
		return roman.toString();
	}

	/**
	 * 罗马转阿拉伯
	 * Converts to Arabic numbers.
	 */
	public static int convertToArabic(String roman) {
		int start = 0, value = 0;
		for (int i = 0; i < LETTERS.length; i++) {
			while (roman.startsWith(LETTERS[i], start)) {
				value += VALUES[i];
				start += LETTERS[i].length();
			}
		}
		return start == roman.length() ? value : -1;
	}
}
