package org.smile.orm;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

import org.smile.commons.ann.NULL;
import org.smile.orm.ann.Association;
import org.smile.orm.mapping.OrmTableMapping;
import org.smile.reflect.ClassTypeUtils;
/**
 *	表对象关联配置
 * @author 胡真山
 *
 */
public class AssociationConfig<O,M> {
	/**
	 * 外键 字段名
	 */
	private String foreignKey;
	/**
	 * 集合的封装类
	 */
	private Class<M> clazz;
	/**
	 * 用于关联的字段
	 */
	private Field field;
	
	private OrmTableMapping<O> oneMapper;
	
	private OrmTableMapping<M> manyMapper;
	/**
	 * 关联的类型 一对多 多对一 一对一
	 * @see Association
	 * */
	private byte type=0;
	
	public String getForeignKey() {
		return foreignKey;
	}
	
	public Class<?> getFieldType() {
		return field.getType();
	}
	
	public Class<M> getClazz() {
		return clazz;
	}
	
	public Field getField() {
		return field;
	}

	public void setForeignKey(String foreignKey) {
		this.foreignKey = foreignKey;
	}

	public AssociationConfig(OrmTableMapping<O> oneMapper,Field field,Class<M> className,String forignKey,byte type){
		this.field=field;
		this.oneMapper=oneMapper;
		field.setAccessible(true);
		Class fieldType=field.getType();
		this.type=type;
		if(className==NULL.class){
			if(Collection.class.isAssignableFrom(fieldType)){
				Class[] generic=ClassTypeUtils.getGeneric(field);
				if(generic!=null){
					this.clazz=generic[0];
				}
			}else if(Map.class.isAssignableFrom(fieldType)){
				Class[] generic=ClassTypeUtils.getGeneric(field);
				if(generic!=null){
					this.clazz=generic[1];
				}
			}else{
				this.clazz=fieldType;
			}
		}else{
			this.clazz=className;
		}
		this.manyMapper= OrmTableMapping.getType(clazz);
		this.foreignKey=forignKey;
	}

	public OrmTableMapping<O> getOneMapper() {
		return oneMapper;
	}

	public OrmTableMapping<M> getManyMapper() {
		return manyMapper;
	}
	
}
