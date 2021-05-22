package org.smile.orm.mapping.property;

import org.smile.db.sql.Mapping;
import org.smile.orm.mapping.OrmComponent;
import org.smile.reflect.Generic;

public abstract class OrmProperty implements Mapping{
	/**
	 * 通用的属性SQL语句占位符
	 */
	protected static final String mappingExp="?";
	/**属性名称*/
	protected String propertyName;
	/**对应数据库列名*/
	protected String columnName;
	/**是否需要持久化*/
	protected boolean persistence;
	/** 属性字段的类型*/
	protected Class<?> fieldType;
	/**属性字段的泛型*/
	protected Generic generic;
	/**
	 * 是不是自动增长的支持
	 */
	protected boolean isAtuoincrement=false;
	/**
	 * 是否是json字段
	 */
	protected boolean jsonStore;
	/**注释*/
	protected String note;
	
	protected OrmComponent component;

	public Class getFieldType() {
		return fieldType;
	}

	public boolean isPersistence() {
		return persistence;
	}
	
	/**
	 * 字段的泛型
	 * @return
	 */
	public Generic getGeneric() {
		return generic;
	}

	public void setPersistence(boolean persistence) {
		this.persistence = persistence;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	
	@Override
	public String getPropertyExp() {
		return mappingExp;
	}
	/**
	 * 是否是自动增长
	 * @return
	 */
	public boolean isAtuoincrement() {
		return isAtuoincrement;
	}
	/**
	 * 设置自动增长支持
	 * @param isAtuoincrement
	 */
	public void setAtuoincrement(boolean isAtuoincrement) {
		this.isAtuoincrement = isAtuoincrement;
	}

	/**
	 * 是否json保存字段
	 * @return
	 */
	public boolean isJsonStore() {
		return jsonStore;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
	public boolean hasNote() {
		return this.note!=null;
	}

	public OrmComponent getComponent() {
		return component;
	}

	public void setComponent(OrmComponent component) {
		this.component = component;
	}
	/**
	 * 是否是组件的字段属性
	 * @return
	 */
	public boolean isComponentProperty() {
		return this.component!=null;
	}
	/**
	 * 读取实体对象的属性值
	 * @param bean
	 * @return
	 */
	public abstract Object readValue(Object bean);
	/**
	 * 往实体对象的属性赋值
	 * @param bean
	 * @param value
	 */
	public abstract void writeValue(Object bean, Object value);
}
