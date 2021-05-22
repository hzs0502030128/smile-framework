package org.smile.util;

import java.util.Date;

/**
 * 数据验证工具类
 * @author 胡真山
 * @Date 2016年1月27日
 */
public class ValidateUtils {
	/**邮箱正则*/
	private static final RegExp EMAIL=new RegExp("^.+@.+\\..*");
	/**没有杠的日期格式*/
	public static final  RegExp YYYYMMDD=new RegExp("[1-9]\\d{3}(0[1-9]|1[0-2])(0[1-9]|[1-2][0-9]|3[0-1])");
	/**有横框的日期格式*/
	public static final  RegExp YYYY_MM_DD=new RegExp("[1-9]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])");
	/**电话号码正则*/
	private static final RegExp TELEPHONE=new RegExp("(^\\d{11}$)|^((\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1})|(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1}))$");
	/**
	 * 非空验证
	 * @param obj
	 * @return
	 */
	public static boolean required(Object obj){
		return StringUtils.isNotNull(obj);
	}
	/**
	 * 区间验证
	 * @param number
	 * @param min
	 * @param max
	 * @return
	 */
	public static boolean range(double number,double min,double max){
		return min<=number&&number<=max;
	}
	/**
	 * 区间验证
	 * @param value
	 * @param min
	 * @param max
	 * @return
	 */
	public static boolean range(Comparable value,Comparable min,Comparable max){
		return min.compareTo(value)<=0&&max.compareTo(value)>=0;
	}
	/**
	 * 长度区间验证
	 * @param string
	 * @param min
	 * @param max
	 * @return
	 */
	public static boolean lengthRange(String string,int min,int max){
		return string.length()>=min&&string.length()<=max;
	}
	/**
	 * 最大长度
	 * @param string
	 * @param max
	 * @return
	 */
	public static boolean maxLength(String string,int max){
		return string.length()<=max;
	}
	/**
	 * 最小长度
	 * @param string
	 * @param min
	 * @return
	 */
	public static boolean minLength(String string,int min){
		return string.length()>=min;
	}
	/**
	 * eqauls验证
	 * @param one
	 * @param to
	 * @return
	 */
	public static boolean equalTo(String one,String to){
		return one.equals(String.valueOf(to));
	}
	
	public static boolean equalTo(Object one,Object two){
		return one.equals(two);
	}
	/**
	 * 数字格式验证
	 * @param number
	 * @return
	 */
	public static boolean number(String number){
		try{
			Double.valueOf(number);
			return true;
		}catch(NumberFormatException e){
			return false;
		}
	}
	/**
	 * 日期格式验证
	 * @param date
	 * @return
	 */
	public static boolean date(String date){
		try{
			return YYYY_MM_DD.matches(date);
		}catch(Exception e){
			return false;
		}
	}
	/**
	 * 电话格式
	 * @param tel
	 * @return
	 */
	public static boolean telephone(String tel){
		return TELEPHONE.test(tel);
	}
	/**
	 * 电子邮件
	 * @param e
	 * @return
	 */
	public static boolean email(String e){
		return EMAIL.test(e);
	}
	
	/**
	 * 时间格式
	 * @param datetime
	 * @return
	 */
	public static boolean datetime(String datetime){
		Date date=DateUtils.parseDate(datetime);
		return date!=null;
	}
	
	/**
	 * 是否合符正则
	 * @param string
	 * @param regexp
	 * @param iscase 是否区分大小写 false是不区分大小写
	 * @return
	 */
	public static boolean regexp(String string,String regexp,boolean iscase){
		RegExp reg=new RegExp(regexp,iscase);
		return reg.test(string);
	}
	/**
	 * 是否符正则表达式
	 * @param string
	 * @param regexp
	 * @return
	 */
	public static boolean regexp(String string,String regexp){
		RegExp reg=new RegExp(regexp);
		return reg.test(string);
	}
}
