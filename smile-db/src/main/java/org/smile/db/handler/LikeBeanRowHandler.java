package org.smile.db.handler;

import java.beans.PropertyDescriptor;

import org.smile.beans.property.LikePropertyConverter;
import org.smile.commons.Column;
import org.smile.db.result.BeanDatabaseColumn;

/**
 * 把结果集转成Bean的RowHandler
 * 
 * @author strive
 *
 */
public class LikeBeanRowHandler extends BeanRowHandler {

	public LikeBeanRowHandler(Class type) {
		this.resultClass=type;
		this.propertyConverter=new DatabaseLikePropertyConverter(type);
	}
	
	protected class DatabaseLikePropertyConverter extends LikePropertyConverter{
		
		public DatabaseLikePropertyConverter(Class c) {
			super(c);
		}

		@Override
		public Column<PropertyDescriptor> newColumn(int index,String key) {
			PropertyDescriptor pd=keyToProperty(key);
			if(pd!=null){
				return new BeanDatabaseColumn(index,pd,key);
			}
			return null;
		}
	}
	
}
