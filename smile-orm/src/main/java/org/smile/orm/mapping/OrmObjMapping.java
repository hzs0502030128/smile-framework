package org.smile.orm.mapping;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.smile.annotation.AnnotationUtils;
import org.smile.collection.ArrayUtils;
import org.smile.orm.OrmInitException;
import org.smile.orm.ann.Component;
import org.smile.orm.ann.Entity;
import org.smile.orm.ann.Mixes;
import org.smile.orm.mapping.flag.PropertyFlag;
import org.smile.orm.mapping.flag.TableFlag;
import org.smile.orm.mapping.property.OrmFieldProperty;
import org.smile.orm.mapping.property.OrmProperty;
import org.smile.reflect.FieldUtils;

/**
 * 不与数据库表对应  用于对结果集处理  
 * @author 胡真山
 *
 * @param <V>
 */
public class OrmObjMapping<V> extends OrmMapping<V>{
	/***
	 * 所有的注解了table的类的解析
	 */
	private final static Map<Class, OrmObjMapping> ormObjectMappings = new ConcurrentHashMap<Class, OrmObjMapping>();
	
	/**所有的组件*/
	private Map<String,OrmComponent> components=new HashMap<>();
	/**此包含的混合映射 */
	protected final Map<Class,OrmMixMapping<V>> mixesOrms=new HashMap<>();
	/**是否存在即时的混合映射*/
	protected boolean immediateMixes=false;
	
	@Override
	public void initType(Class clazz) throws MappingException{
		rawClass = clazz;
		this.tableFlag=flagHandler.getTableFlag(clazz);
		if (tableFlag == null) {
			throw new MappingException(clazz+" 没有对应的注解:"+Entity.class);
		} else {
			name = tableFlag.getName();
		}
		initProperties();
		initMixOrmMappings();
	}
	
	/**
	 * 初始化混合映射
	 * @throws MappingException
	 */
	protected void initMixOrmMappings() throws MappingException {
		Class[] interfaces=this.rawClass.getInterfaces();
		if(ArrayUtils.notEmpty(interfaces)) {
			for(Class clazz:interfaces) {
				Mixes mixes=AnnotationUtils.getAnnotation(clazz, Mixes.class);
				if(mixes!=null) {
					OrmMixMapping<V> mixMapping=new OrmMixMapping<V>(this);
					mixMapping.initType(clazz);
					mixMapping.initMixesValue(mixes);
					this.mixesOrms.put(clazz, mixMapping);
				}
			}
		}
	}
	
	
	/**初始化属性信息*/
	@Override
	protected void initProperties() {
		Map<String,Field> fields = FieldUtils.getAnyNoStaticField(rawClass);
		for (Field field : fields.values()) {
			try {
				PropertyFlag propertyFlag = flagHandler.getPropertyFlag(tableFlag,field);
				if (propertyFlag != null) {
					OrmFieldProperty property=propertyFlag.getProperty(this);
					propertyMap.put(property.getPropertyName(), property);
					columnMap.put(property.getColumnName(), property);
					property.setPersistence(true);
					initNote(field, property);
				}else {
					initComponent(field);
				}
			} catch (MappingException e) {
				throw new OrmInitException("初始化 OrmObjMapper 失败："+rawClass,e);
			}
		}
	}
	/**
	 * 初始化组件字段
	 * @param field
	 * @throws MappingException 
	 */
	protected void initComponent(Field field) throws MappingException {
		Component componentAnn=AnnotationUtils.getAnnotation(field, Component.class);
		if(componentAnn!=null) {
			OrmComponent<V, Object> component=new OrmComponent<V, Object>(this,field);
			component.initType(field.getType());
			this.components.put(component.getName(), component);
			//把组件的字段添加到mapping中
			for(OrmProperty op:component.getPropertyMap().values()) {
				this.propertyMap.put(op.getPropertyName(), op);
				this.columnMap.put(op.getColumnName(), op);
				if(op.hasNote()) {
					this.notedMap.put(op.getPropertyName(), op);
				}
			}
		}
	}
	/**
	 * 获取混合映射
	 * @return
	 */
	public Collection<OrmMixMapping<V>> getOrmMixMappings(){
		return this.mixesOrms.values();
	}
	/**
	 * 获取混合映射配置
	 * @param mixesClass
	 * @return
	 */
	public OrmMixMapping<V> getOrmMixMapping(Class mixesClass){
		return this.mixesOrms.get(mixesClass);
	}
	/**
	 * 是否存在混合映射
	 * @return
	 */
	public boolean hasMixes() {
		return !this.mixesOrms.isEmpty();
	}
	
	
	/**
	 * 获取映射配置对象
	 * @param mapperClass
	 * @return
	 */
	public static OrmObjMapping getOrmMapper(Class mapperClass){
		OrmObjMapping ormMapper = ormObjectMappings.get(mapperClass);
		if (ormMapper != null) {
			return ormMapper;
		}
		synchronized (ormObjectMappings) {
			// 是个映射的时候
			TableFlag tableFlag=flagHandler.getTableFlag(mapperClass);
			if (tableFlag != null) {
				if (tableFlag.isTable()) {// 对应数据库表的时候
					ormMapper = OrmTableMapping.getType(mapperClass);
				} else{
					ormMapper = new OrmObjMapping();
					try {
						ormMapper.initType(mapperClass);
						logger.info("init mapper success type " + ormMapper.getClass() + " " + ormMapper.getRawClass().getName() + "-->" + ormMapper.getName());
					} catch (MappingException e) {
						throw new OrmInitException("init class mapping " + mapperClass, e);
					}
				}
				ormObjectMappings.put(mapperClass, ormMapper);
			}
		}
		return ormMapper;
	}

	public boolean hasImmediateMixes() {
		return immediateMixes;
	}

	public void hasImmediateMixes(boolean immediateMixes) {
		this.immediateMixes = immediateMixes;
	}
}
