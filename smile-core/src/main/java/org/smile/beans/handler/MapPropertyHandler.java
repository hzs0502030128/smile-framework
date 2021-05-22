package org.smile.beans.handler;

import java.util.Map;

import org.smile.beans.BeanProperties;
import org.smile.beans.FieldDeclare;

public class MapPropertyHandler extends DeclarePropertyHandler<Map>{
	/**默认*/
	public static MapPropertyHandler DEFAULT=new MapPropertyHandler();
	
	public MapPropertyHandler(BeanProperties properties){
		this.declare=new FieldDeclare<Object>(Object.class);
		this.properties=properties;
	}
	
	public MapPropertyHandler(){
		this(new BeanProperties());
	}

}
