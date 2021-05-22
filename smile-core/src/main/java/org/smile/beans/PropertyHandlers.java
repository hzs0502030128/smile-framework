package org.smile.beans;

import java.util.Map;

import org.smile.beans.handler.CollectionPropertyHandler;
import org.smile.beans.handler.MapBeanPropertyHandler;
import org.smile.beans.handler.MapPropertyHandler;

public class PropertyHandlers {
	/**
	 * 以类型获取一个属性处理器
	 * @param type
	 * @return
	 */
	public static PropertyHandler getHanlder(Class type){
		if(Map.class.isAssignableFrom(type)){
			return MapPropertyHandler.DEFAULT;
		}else if(FieldDeclare.canLookAsCollection(type)){
			return CollectionPropertyHandler.DEFAULT;
		}else{
			return BeanProperties.NORAL;
		}
	}
	/**
	 * 获取一个属性处理器的实例
	 * @param type 操作对象的类型
	 * @param noExitisError 属性不存在时不抛出异常
	 * @return
	 */
	public static PropertyHandler getHanlder(Class type,boolean noExitisError){
		if(Map.class.isAssignableFrom(type)){
			return new MapPropertyHandler(new BeanProperties(noExitisError));
		}else if(FieldDeclare.canLookAsCollection(type)){
			return new CollectionPropertyHandler(new BeanProperties(noExitisError));
		}else if(MapBean.class.isAssignableFrom(type)){
			return noExitisError?MapBeanPropertyHandler.DEFAULT:MapBeanPropertyHandler.CAN_NO_PROPERTY;
		}else{
			return new BeanProperties(noExitisError);
		}
	}
	
	public static PropertyHandler getHanlder(Class type,BeanProperties beanProperties){
		if(Map.class.isAssignableFrom(type)){
			return new MapPropertyHandler(beanProperties);
		}else if(FieldDeclare.canLookAsCollection(type)){
			return new CollectionPropertyHandler(beanProperties);
		}else if(MapBean.class.isAssignableFrom(type)){
			return MapBeanPropertyHandler.CAN_NO_PROPERTY;
		}else{
			return beanProperties;
		}
	}
}
