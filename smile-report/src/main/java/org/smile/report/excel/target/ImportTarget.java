package org.smile.report.excel.target;

import java.util.Map;

import org.smile.beans.BeanProperties;
import org.smile.beans.MapBean;
import org.smile.beans.PropertyValue;
import org.smile.beans.converter.BeanException;

public abstract class ImportTarget {
	/**
	 * 创建新对象 
	 * 用来封装一行数据
	 * @return
	 */
	public abstract Object newTargetInstanse();
	
	public abstract BeanProperties getBeanProperties();
	
	public void setFieldValue(Object oneData, String field, Object value) throws BeanException{
		if(oneData instanceof Map){
			new PropertyValue(oneData).setPropertyValue(getBeanProperties(),field, value);
		}else if(oneData instanceof MapBean){
			((MapBean) oneData).set(field, value);
		}else{
			getBeanProperties().setFieldValue(field, value, oneData);
		}
	}
	
	public PropertyValue getFieldValue(Object targetObject, String name, boolean needInit) throws BeanException{
		if(targetObject instanceof Map){
			Object obj=((Map) targetObject).get(name);
			if(obj==null){
				return null;
			}
			return new PropertyValue(obj);
		}else if(targetObject instanceof MapBean){
			PropertyValue value=((MapBean) targetObject).getPropertyValue(name);
			if(needInit){
				value.value(value.getDeclare().newInstance());
			}
			return value;
		}else{
			return getBeanProperties().getFieldValue(targetObject, name, needInit);
		}
	}
}
