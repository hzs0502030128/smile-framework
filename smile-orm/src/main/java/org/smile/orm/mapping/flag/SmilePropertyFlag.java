package org.smile.orm.mapping.flag;

import java.lang.reflect.Field;

import org.smile.annotation.AnnotationUtils;
import org.smile.beans.converter.BeanException;
import org.smile.commons.IdGenerator;
import org.smile.commons.ann.NULL;
import org.smile.db.handler.HumpKeyColumnSwaper;
import org.smile.orm.OrmInitException;
import org.smile.orm.ann.Association;
import org.smile.orm.ann.Component;
import org.smile.orm.ann.HumpColumns;
import org.smile.orm.ann.Property;
import org.smile.orm.mapping.OrmObjMapping;
import org.smile.orm.mapping.property.OrmFieldProperty;
import org.smile.orm.mapping.property.OrmIdProperty;
import org.smile.reflect.ClassTypeUtils;
import org.smile.util.StringUtils;
import org.smile.util.UUIDGenerator;

public  class SmilePropertyFlag extends PropertyFlag{
	
	/**主键生成器*/
	public static IdGenerator uuidGenerator=new UUIDGenerator();
	/**
	 * 是否存数据库
	 */
	private boolean store=true;
	/**是否是uuid字段*/
	private boolean uuid=false;
	/**id生成器*/
	private IdGenerator idGenerator;
	
	@Override
	public  boolean checkFlag(TableFlag tableFlag,Field field){
		this.name=field.getName();
		this.field=field;
		Property fieldAnno =AnnotationUtils.getAnnotation(field,Property.class);
		if(fieldAnno!=null){
			this.column=fieldAnno.column();
			this.flaged=true;
			this.store=fieldAnno.store();
			this.primaryKey=fieldAnno.primaryKey();
			this.autoincrement=fieldAnno.autoincrement();
			this.uuid=fieldAnno.uuid();
			if(fieldAnno.idGenerator()!=NULL.class) {
				try {
					idGenerator=(IdGenerator)ClassTypeUtils.newInstance(fieldAnno.idGenerator());
				} catch (BeanException e) {
					throw new OrmInitException("id generator create error ",e);
				}
			}else if(this.uuid) {
				idGenerator=uuidGenerator;
			}
		}else if(tableFlag.isHumpColumns()){
			//不是关联其它表的时
			if(AnnotationUtils.getAnnotation(field, Component.class)==null && AnnotationUtils.getAnnotation(field, Association.class)==null){
				//是组件或其它表关联时不映射表字段
				//当是驼峰规则时,设置成是映射字段
				flaged = true;
			}
		}
		if(flaged&&StringUtils.isEmpty(column)){
			column=HumpKeyColumnSwaper.instance.KeyToColumn(name);
		}
		return flaged;
	}

	@Override
	public OrmIdProperty getIdProperty(OrmFieldProperty property) {
		OrmIdProperty idProperty=new OrmIdProperty(property);
		idProperty.setUuid(uuid);
		idProperty.setIdGenerator(idGenerator);
		return idProperty;
	}

	@Override
	public OrmFieldProperty getProperty(OrmObjMapping mapping) {
		OrmFieldProperty property=mapping.newOrmProperty(field);
		property.setColumnName(column);
		property.setPropertyName(name);
		property.setAtuoincrement(autoincrement);
		property.setPersistence(store);
		return property;
	}
}
