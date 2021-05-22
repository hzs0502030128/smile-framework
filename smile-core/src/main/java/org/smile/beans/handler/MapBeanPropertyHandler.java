package org.smile.beans.handler;

import org.smile.beans.MapBean;
import org.smile.beans.PropertyHandler;
import org.smile.beans.PropertyValue;
import org.smile.beans.converter.BeanException;

public class MapBeanPropertyHandler implements PropertyHandler<MapBean>{
	
	public static MapBeanPropertyHandler DEFAULT=new MapBeanPropertyHandler(true);
	public static MapBeanPropertyHandler CAN_NO_PROPERTY=new MapBeanPropertyHandler(false);
	/**
	 * 不存在字段时是否异常
	 */
	protected boolean noPropertyError=false;
	
	public MapBeanPropertyHandler(boolean noPropertyError){
		this.noPropertyError=noPropertyError;
	}
	
	@Override
	public Object getExpFieldValue(MapBean target, String exp) throws BeanException {
		int index = exp.indexOf(".");
		if (index > 0) {
			String name = exp.substring(0, index);
			PropertyValue value = target.getPropertyValue(name);
			if(value==null&&noPropertyError){
				throw new BeanException("不存在的字段"+name);
			}
			if (PropertyValue.notNull(value)) {
				String subName = exp.substring(index + 1);
				return value.getPropertyValue(target.getBeanProperties(), subName);
			}
			return null;
		}else{
			PropertyValue value = target.getPropertyValue(exp);
			if(value==null){
				if(noPropertyError){
					throw new BeanException("不存在的字段"+exp);
				}else{
					return null;
				}
			}else{
				return value.value();
			}
		}
	}

	@Override
	public void setExpFieldValue(MapBean target, String exp, Object value) throws BeanException {
		String name=exp;
		int index = exp.indexOf(".");
		if (index > 0) {
			name = exp.substring(0, index);
			PropertyValue pv = target.getPropertyValue(name);
			if(pv==null){
				if(noPropertyError){
					throw new BeanException(target.getMapBeanClass()+"不存在的字段"+name);
				}
			}else{
				pv.setPropertyValue(target.getBeanProperties(), exp.substring(index + 1), value);
			}
		}else{
			PropertyValue pv = target.getPropertyValue(exp);
			if(pv==null){
				if(noPropertyError){
					throw new BeanException("不存在的字段"+exp);
				}
			}else{
				pv.value(target.getBeanProperties(), value);
			}
		}
	}
	
	

}
