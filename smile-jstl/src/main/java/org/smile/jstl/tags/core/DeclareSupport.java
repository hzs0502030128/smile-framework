
package org.smile.jstl.tags.core;

import org.smile.beans.converter.BaseTypeConverter;
import org.smile.beans.converter.BasicConverter;
import org.smile.jstl.tags.JSONParseSupport;
import org.smile.reflect.ClassTypeUtils;
import org.smile.util.DateUtils;

/**
 * @author 胡真山
 *
 */
public class DeclareSupport extends JSONParseSupport{
	/**
	 * 类型转换
	 * @param type
	 * @param value
	 * @return
	 * @throws Exception
	 */
	protected static Object convert(String type,Object value) throws Exception{
		Object realValue=null;
		Class basicClass=ClassTypeUtils.getBasicTypeClass(type);
		if(basicClass!=null){
			realValue=BaseTypeConverter.getInstance().convert(basicClass, value);
		}else if(ClassTypeUtils.isMapName(type)){
			realValue=parseToMap(value);
		}else if(ClassTypeUtils.isStringName(type)){
			realValue=BasicConverter.getInstance().convert(String.class, value);
		}else if(ClassTypeUtils.isDateName(type)){
			realValue=DateUtils.convertToDate(value);
		}
		return realValue;
	}
	
}