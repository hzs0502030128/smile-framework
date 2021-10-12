package org.smile.jstl.functions;


import java.sql.SQLException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import org.smile.util.ObjectLenUtils;
/**
 * 函数
 * @author 胡真山
 *
 */
public class CommonFunction {
	/**
	 * 得到一个对象的长度
	 * @param o 对象
	 * @return 整型长度
	 */
	public static int len(Object o){
		if(o==null){
			return 0;
		}else{
			try{
				return ObjectLenUtils.len(o);
			}catch(Exception e){
				return 1;
			}
		}
	}
	/**
	 * 是否为空
	 * @param str
	 * @return
	 */
	public static boolean empty(Object obj){
		if(len(obj)>0){
			return false;
		}else{
			return true;
		}
	}
	/**
	 * 得到参数的对应索引的值
	 * @param o
	 * @param index
	 * @return
	 * @throws SQLException
	 */
	public static Object get(Object o,int index){
		if(o==null){
			return null;
		}else{
			return ObjectLenUtils.get(o, index);
		}
	}
	/**
	 * 日期年份
	 * @param date
	 * @return
	 */
	public static int getYear(Date date){
		Calendar c=Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.YEAR);
	}
	
	/**
	 * 日期月份
	 * @param date
	 * @return
	 */
	public static int getMonth(Date date){
		Calendar c=Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.MONTH)+1;
	}
	/**
	 * 日期天
	 * @param date
	 * @return
	 */
	public static int getDate(Date date){
		Calendar c=Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.DATE);
	}
	/**
	 * 系统时期
	 * @param number
	 * @param pattern
	 * @return
	 * @throws ParseException 
	 */
	public static Date newDate(){
		return new Date();
	}
}
