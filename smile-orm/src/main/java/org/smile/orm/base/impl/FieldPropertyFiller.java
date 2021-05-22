package org.smile.orm.base.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;

import org.smile.beans.BeanUtils;
import org.smile.beans.converter.BeanException;
import org.smile.db.sql.parameter.AbstractParameterFiller;
import org.smile.orm.mapping.OrmMapping;
import org.smile.orm.mapping.OrmTableMapping;
import org.smile.orm.mapping.property.OrmIdProperty;
import org.smile.orm.mapping.property.OrmProperty;

public class FieldPropertyFiller extends AbstractParameterFiller{
	/***
	 * 插入操作时设置ps值
	 * @param ormMapper
	 * @param ps
	 * @param bean
	 * @throws SQLException
	 */
	public void fillInsertPreparedStatement(OrmTableMapping ormMapper,PreparedStatement ps, Object bean) throws SQLException {
		int index = 1;
		Collection<OrmProperty> propertys=ormMapper.columnPropertys();
		if(ormMapper.hasPrimaryKey()) {
			OrmIdProperty idProperty=ormMapper.getPrimaryProperty();
			if(idProperty.hasIdGenerator()) {//存在id生成器时id为空会自动生成id值
				Object id=ormMapper.getPrimaryKeyValue(bean);
				if(id==null){
					idProperty.getProperty().writeValue(bean,idProperty.generateId() );
				}
			}
		}
		for (OrmProperty property : propertys) {
			if(property.isPersistence()){
				fillPreparedStatement(ps, bean, property, index);
				index++;
			}
		}
	}
	
	/***
	 * 插入操作时设置ps值
	 * @param ormMapper
	 * @param ps
	 * @param bean 不是当前映射类的对象
	 * @throws SQLException
	 */
	public void fillInsertOtherObjPreparedStatement(OrmTableMapping ormMapper,PreparedStatement ps, Object bean) throws SQLException {
		Collection<OrmProperty> propertys=ormMapper.columnPropertys();
		if(ormMapper.hasPrimaryKey()){
			OrmIdProperty idProperty=ormMapper.getPrimaryProperty();
			if(idProperty.hasIdGenerator()) {
				Object id=ormMapper.getPrimaryKeyValue(bean);
				if(id==null){
					try {
						BeanUtils.setValue(bean, idProperty.getPropertyName(), idProperty.generateId());
					} catch (BeanException e) {
						throw new SQLException("auto uuid error",e);
					}
				}
			}
		}
		int index = 1;
		for (OrmProperty property : propertys) {
			if(property.isPersistence()){
				fillOtherObjPreparedStatement(ps, bean, property, index);
				index++;
			}
		}
	}
	
	public void fillPreparedStatement(OrmMapping ormMapping,PreparedStatement ps,Object bean, String... propertyNames) throws SQLException {
		int index = 1;
		Map<String, OrmProperty> propertys=ormMapping.getPropertyMap();
		for (String propertyName : propertyNames) {
			OrmProperty property = propertys.get(propertyName);
			if (property == null) {
				logger.error("数据名为空:" + propertyName);
				continue;
			}
			if (property.isPersistence()) {
				fillPreparedStatement(ps, bean, property, index);
				index++;
			}
		}
	}
	/**
	 * 更新操作时ps赋值
	 * @param ormMapper
	 * @param ps
	 * @param bean
	 * @param properties 要更新的属性字段
	 * @throws SQLException
	 */
	public void fillUpdateByIdPreparedStatement(OrmTableMapping ormMapper,PreparedStatement ps,Object bean, Collection<OrmProperty> properties) throws SQLException {
		int index = 1;
		for (OrmProperty property : properties) {
			if(property.isPersistence()){
				fillPreparedStatement(ps, bean, property, index);
				index++;
			}
		}
		fillPreparedStatement(ps,bean,ormMapper.getPrimaryProperty().getProperty(),index);
	}
	/**
	 * 更新操作时ps赋值
	 * @param ormMapper
	 * @param ps
	 * @param bean
	 * @param properties 要更新的属性字段
	 * @throws SQLException
	 */
	public void fillOtherObjUpdateByIdPreparedStatement(OrmTableMapping ormMapper,PreparedStatement ps,Object bean, Collection<OrmProperty> properties) throws SQLException {
		int index = 1;
		for (OrmProperty property : properties) {
			if(property.isPersistence()){
				fillOtherObjPreparedStatement(ps, bean, property, index);
				index++;
			}
		}
		fillOtherObjPreparedStatement(ps,bean,ormMapper.getPrimaryProperty().getProperty(),index);
	}
	/**
	 * 赋值一个索引的值
	 * @param ps
	 * @param bean
	 * @param property
	 * @param index
	 * @throws SQLException
	 */
	public void fillPreparedStatement(PreparedStatement ps, Object bean,OrmProperty property, int index) throws SQLException {
		Class fieldType=property.getFieldType();
		Object value = property.readValue(bean);
		fillPreparedStatement(ps, fieldType, value, index);
	}
	
	/**
	 * 赋值一个索引的值
	 * @param ps
	 * @param bean 不是当前映射类的对象
	 * @param property
	 * @param index
	 * @throws SQLException
	 * @throws BeanException 
	 */
	public void fillOtherObjPreparedStatement(PreparedStatement ps, Object bean,OrmProperty property, int index) throws SQLException{
		Class fieldType=property.getFieldType();
		try {
			Object value = BeanUtils.getValue(bean, property.getPropertyName());
			fillPreparedStatement(ps, fieldType, value, index);
		} catch (BeanException e) {
			throw new SQLException(e);
		}
	}
	/**
	 * 赋值一个索引的值
	 * @param ps 
	 * @param fieldType 类型
	 * @param value
	 * @param index
	 * @throws SQLException
	 */
	public void fillPreparedStatement(PreparedStatement ps,Class fieldType,Object value,int index) throws SQLException{
		fillObject(ps, index, value);
	}

	@Override
	public void fillObject(PreparedStatement ps, Object value) throws SQLException {
		throw new SQLException("no support this method");
	}
}
