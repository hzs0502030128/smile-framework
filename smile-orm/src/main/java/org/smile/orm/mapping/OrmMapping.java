package org.smile.orm.mapping;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.smile.annotation.AnnotationUtils;
import org.smile.beans.converter.BeanException;
import org.smile.beans.property.PropertyConverter;
import org.smile.collection.KeyNoCaseHashMap;
import org.smile.commons.Column;
import org.smile.commons.ServiceFinder;
import org.smile.commons.ann.Note;
import org.smile.db.result.DatabaseColumn;
import org.smile.db.result.QueryResult;
import org.smile.db.result.ResultParser;
import org.smile.log.LoggerHandler;
import org.smile.orm.OrmInitException;
import org.smile.orm.mapping.flag.MapperFlagHandler;
import org.smile.orm.mapping.flag.SmileMapperFlagHandler;
import org.smile.orm.mapping.flag.TableFlag;
import org.smile.orm.mapping.property.OrmBeanProperty;
import org.smile.orm.mapping.property.OrmFieldProperty;
import org.smile.orm.mapping.property.OrmProperty;
import org.smile.orm.mapping.property.OrmPropertyColumn;
import org.smile.reflect.ClassTypeUtils;

/**
 * 类的映射
 * @author 胡真山
 * @param <V> 映射的类
 */
public abstract class OrmMapping<V> implements LoggerHandler,PropertyConverter<OrmProperty>{
	/**映射标记处理类*/
	public static MapperFlagHandler flagHandler=null;
	/**映射标识*/
	protected  TableFlag tableFlag;
	
	static{
		String flagHandlerImplClass=ServiceFinder.findImpl(MapperFlagHandler.class.getName(), SmileMapperFlagHandler.class.getName());
		try {
			flagHandler=ClassTypeUtils.newInstance(flagHandlerImplClass);
		} catch (BeanException e) {
			throw new OrmInitException("mapper flag handler init fail "+flagHandlerImplClass,e);
		}
	}
	/**用于映射的类*/
	protected Class<V> rawClass;
	/***名称*/
	protected String name;
	/***
	 * 一个类型的属性名
	 */
	protected final Map<String, OrmProperty> propertyMap = new KeyNoCaseHashMap<OrmProperty>();
	/***
	 * 以数据库列名做为key一个集合
	 */
	protected final Map<String, OrmProperty> columnMap = new KeyNoCaseHashMap<OrmProperty>();
	/**
	 * 添加了注解的字段
	 */
	protected final  Map<String, OrmProperty> notedMap = new KeyNoCaseHashMap<OrmProperty>();
	
	/**
	 *       属性名的映射
	 * @return
	 */
	public Map<String, OrmProperty> getPropertyMap() {
		return propertyMap;
	}

	public void setRawClass(Class<V> rawClass) {
		this.rawClass = rawClass;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	/**以属性名获取属性映射对象*/
	public OrmProperty getProperty(String propertyName) {
		return propertyMap.get(propertyName);
	}
	
	/**以数据库列名获取属性映射对象*/
	public OrmProperty getPropertyByColumn(String columnName) {
		return columnMap.get(columnName);
	}

	/**
	 * 设置一个发展的值
	 * @param bean 
	 * @param propertyName
	 * @param value
	 */
	public void setPropertyValue(V bean, String propertyName, Object value) {
		OrmProperty property = propertyMap.get(propertyName);
		property.writeValue(bean, value);
	}
	
	/**初始化*/
	public abstract void initType(Class clazz) throws MappingException;

	/**初始化属性信息*/
	protected abstract void initProperties() ;
	
	/**
	 * 解析结果集
	 * @param parser
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	public V parseResultSet(ResultParser parser, QueryResult rs) throws SQLException {
		return parseResultSet(parser, newRawInstance(), rs);
	}
	
	/**
	 * 从结果集中解析数据对一个对象中
	 * @param parser
	 * @param bean
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	public V parseResultSet(ResultParser parser,V bean, QueryResult rs) throws SQLException {
		List<Column> columns=rs.getIndexedColumns(this);
		for (Column<OrmProperty> column:columns) {
			parser.parseResultSet(rs.getResultSet(), bean, (DatabaseColumn)column);
		}
		return bean;
	}
	/**
	 * 新构构建一个对象
	 * @return
	 * @throws SQLException 
	 */
	public V newRawInstance(){
		try {
			return this.rawClass.newInstance();
		}catch (Exception e) {
			throw new OrmInitException("new instance mapper obj error "+rawClass, e);
		}
	}

	public Class<V> getRawClass() {
		return rawClass;
	}
	
	public boolean hasOneToMany(){
		return false;
	}

	@Override
	public OrmProperty keyToProperty(String key) {
		return this.columnMap.get(key);
	}
	/***
	 * 映射对象名称
	 * @return
	 */
	public Set<String> getPropertyNames(){
		return this.propertyMap.keySet();
	}
	
	@Override
	public String propertyToKey(OrmProperty property) {
		return property.getColumnName();
	}

	@Override
	public Column<OrmProperty> newColumn(int index, String key) {
		OrmProperty property=keyToProperty(key);
		if(property!=null){
			return new OrmPropertyColumn(index, property);
		}
		return null;
	}

	/**
	 * 所有标记note的字段
	 * @return
	 */
	public Collection<OrmProperty> getNotedPropertys(){
		return notedMap.values();
	}
	/**
	 * 初始化字段批注
	 * @param field
	 * @param property
	 */
	protected void initNote(Field field,OrmProperty property){
		//注释
		Note note=AnnotationUtils.getAnnotation(field,Note.class);
		if(note!=null){
			property.setNote(note.value());
			notedMap.put(property.getPropertyName(), property);
		}
	}
	
	protected void initNote(Method method,OrmProperty property) {
		//注释
		Note note=AnnotationUtils.getAnnotation(method,Note.class);
		if(note!=null){
			property.setNote(note.value());
			notedMap.put(property.getPropertyName(), property);
		}
	}
	
	/**
	 * 创建字段属性封装对象
	 * @param field
	 * @return
	 */
	public OrmFieldProperty newOrmProperty(Field field){
		return new OrmFieldProperty(field);
	}
	/**
	 * 新建一个属性映射实例
	 * @param property
	 * @return
	 */
	public OrmBeanProperty newOrmProperty(PropertyDescriptor property) {
		return new OrmBeanProperty(property);
	}
}
