package org.smile.orm.mapping.property;

import org.smile.db.result.AbstractDatabaseColumn;
import org.smile.reflect.Generic;

/**
 * 以orm 方法操作属性
 * @author 胡真山
 * @Date 2016年1月8日
 */
public class OrmPropertyColumn extends  AbstractDatabaseColumn<OrmProperty>{
	/**字段映射属性*/
	private OrmProperty property;
	
	@Override
	public int getIndex() {
		return index;
	}
	/**
	 * orm字段映射列
	 * @param index
	 * @param property
	 */
	public OrmPropertyColumn(int index,OrmProperty property){
		this.index=index;
		this.property=property;
		this.jsonStore=property.isJsonStore();
		this.name=property.getPropertyName();
		this.columnName=property.getColumnName();
	}

	@Override
	public Class getPropertyType() {
		return property.getFieldType();
	}

	@Override
	public OrmProperty getProperty() {
		return property;
	}

	@Override
	public void writeValue(Object target, Object value){
		property.writeValue(target, value);
	}

	@Override
	public Object readValue(Object target){
		return property.readValue(target);
	}

	@Override
	public String getColumnName() {
		return property.getColumnName();
	}

	@Override
	public boolean isJsonStore() {
		return property.isJsonStore();
	}

	@Override
	public Generic getGeneric() {
		return property.getGeneric();
	}

}
