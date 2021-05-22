package org.smile.beans.converter;

import org.smile.beans.converter.type.BasicArrayConverter;
/***
 * 基础数据数组转换类
 * @author 胡真山
 * 2015年9月23日
 */
public class BaseArrayTypeConverter extends AbstractConverter {

	private static BaseArrayTypeConverter  instance;
	
	public static  BaseArrayTypeConverter getInstance(){
		if(instance==null){
			instance=new BaseArrayTypeConverter();
		}
		return instance;
	}
	/**注册默认转换*/
	protected void regsiterDefaultConverter(){
		regsiterTypeConverter(new BasicArrayConverter(int.class));
		regsiterTypeConverter(new BasicArrayConverter(byte.class));
		regsiterTypeConverter(new BasicArrayConverter(short.class));
		regsiterTypeConverter(new BasicArrayConverter(long.class));
		regsiterTypeConverter(new BasicArrayConverter(float.class));
		regsiterTypeConverter(new BasicArrayConverter(double.class));
		regsiterTypeConverter(new BasicArrayConverter(char.class));
		regsiterTypeConverter(new BasicArrayConverter(boolean.class));
	}

}
