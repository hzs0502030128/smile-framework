package org.smile.jstl.functions;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.smile.commons.Strings;
import org.smile.log.LoggerHandler;
import org.smile.math.NumberUtils;
import org.smile.util.DateUtils;
/**
 * 格式化函数
 * @author 胡真山
 *
 */
public class FormatFunction implements LoggerHandler{
	/**
	 * 日期格式化
	 * @param date
	 * @param pattern
	 * @return
	 * @throws Exception 
	 */
	public static String formatDate(Object date,String pattern) throws Exception{
		if(date==null) return Strings.BLANK;
		if(date instanceof Date){
			return DateUtils.formatDate((Date)date,pattern);
		}else if(date.getClass().getName().equals("oracle.sql.TIMESTAMP")){
			try { 
				Class clz = date.getClass(); 
				Method m = clz.getMethod("timestampValue"); 
				return DateUtils.formatDate((Date)(Timestamp) m.invoke(date),pattern); 
			} catch (Exception e) { 
				throw new Exception("格式化["+date+"] to Date use ["+pattern+"]失败",e);
			} 
		}else if(date instanceof Long ){
			return DateUtils.formatDate(new Date((Long)date),pattern);
		}else if(date instanceof Integer){
			return  DateUtils.formatDate(new Date((Integer)date),pattern);
		}else{
			throw new Exception("未知的日期类型,格式化["+date+"] to Date use ["+pattern+"]失败");
		}
	}
	/**
	 * 数字式化
	 * @param number
	 * @param pattern
	 * @return
	 * @throws Exception 
	 */
	public static String formatNumber(Object number,String pattern) throws Exception{
		return NumberUtils.format(number, pattern);
	}
	/**
	 * 解析一个时间
	 * @param number
	 * @param pattern
	 * @return
	 * @throws ParseException 
	 */
	public static Date parseDate(String date,String pattern) throws ParseException{
		DateFormat df=new SimpleDateFormat(pattern);
		return df.parse(date);
	}
}
