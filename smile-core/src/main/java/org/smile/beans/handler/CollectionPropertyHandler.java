package org.smile.beans.handler;

import java.util.Collection;

import org.smile.beans.BeanProperties;
import org.smile.beans.FieldDeclare;

public class CollectionPropertyHandler extends DeclarePropertyHandler<Collection>{
	/**列表集合操作*/
	public static final CollectionPropertyHandler DEFAULT=new CollectionPropertyHandler();
	
	public CollectionPropertyHandler(BeanProperties properties){
		this.declare=new FieldDeclare(Collection.class);
		this.properties=properties;
	}
	
	public CollectionPropertyHandler(){
		this.declare=new FieldDeclare(Collection.class);
		this.properties=new BeanProperties();
	}
}
