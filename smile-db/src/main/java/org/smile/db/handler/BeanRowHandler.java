package org.smile.db.handler;

import java.beans.PropertyDescriptor;
import java.sql.SQLException;
import java.util.List;

import org.smile.beans.converter.BeanException;
import org.smile.beans.property.NoCasePropertyConverter;
import org.smile.beans.property.PropertyConverter;
import org.smile.commons.Column;
import org.smile.db.result.BeanDatabaseColumn;
import org.smile.db.result.DatabaseColumn;
import org.smile.db.result.QueryResult;
import org.smile.db.result.ResultUtils;
import org.smile.reflect.ClassTypeUtils;

/**
 * 把结果集转成Bean的RowHandler
 * 字段与属性的转换时不区分大小写
 * @author strive
 *
 */
public class BeanRowHandler extends RowHandler {
	/**属性转换器*/
	protected PropertyConverter propertyConverter;
	
	public BeanRowHandler(Class type){
		this.resultClass=type;
		this.propertyConverter=new DatabaseBeanPropertyConverter(type);
	}
	
	protected BeanRowHandler(){}
	
	 
	@Override
	public <E> E handle(QueryResult rs) throws SQLException {
		Object bean;
		try {
			bean = ClassTypeUtils.newInstance(this.resultClass);
			List<Column> columnIndexProperty=rs.getIndexedColumns(propertyConverter);
	        for (Column<PropertyDescriptor> column:columnIndexProperty) {
	        	DatabaseColumn dbColumn=(DatabaseColumn)column;
	        	dbColumn.parseResultColumn(ResultUtils.CONVERTER, rs.getResultSet(), bean);
			}
	        return (E)bean;
		} catch (BeanException e) {
			throw new SQLException(e);
		}
	}
	
	protected class DatabaseBeanPropertyConverter extends NoCasePropertyConverter{
		
		protected DatabaseBeanPropertyConverter(Class type){
			super(type);
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
