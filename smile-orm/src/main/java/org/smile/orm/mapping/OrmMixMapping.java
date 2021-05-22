package org.smile.orm.mapping;

import java.beans.PropertyDescriptor;
import java.util.Set;

import org.smile.annotation.AnnotationUtils;
import org.smile.beans.BeanInfo;
import org.smile.commons.ann.NULL;
import org.smile.db.handler.HumpKeyColumnSwaper;
import org.smile.db.sql.BoundSql;
import org.smile.db.sql.NamedBoundSql;
import org.smile.orm.OrmInitException;
import org.smile.orm.ann.Mixes;
import org.smile.orm.ann.Property;
import org.smile.orm.base.EnableSupportDAO;
import org.smile.orm.mapping.property.OrmBeanProperty;
import org.smile.orm.mapping.property.OrmProperty;
import org.smile.orm.record.OrmRecordConfig;
import org.smile.util.StringUtils;

public class OrmMixMapping<V> extends OrmMapping<V>{

	/**bean信息*/
	private BeanInfo mixBeanInfo;
	/**
	 * 主映射
	 */
	private OrmObjMapping<V> mainMapping;
	
	protected String sql;
	
	protected Class entityClass;
	
	protected Class mixSourceClass;
	
	protected String primaryKey;
	/**是不是即时加载*/
	protected boolean immediate=false;
	
	protected EnableSupportDAO daoSupport=OrmRecordConfig.getInstance().getOrmDaoSupport();
	
	
	public OrmMixMapping(OrmObjMapping<V> mainMapping) {
		this.mainMapping=mainMapping;
	}
	
	@Override
	public void initType(Class clazz) throws MappingException {
		this.mixSourceClass=clazz;
		this.mixBeanInfo=BeanInfo.getInstance(clazz);
		initProperties();
	}
	
	public void initMixesValue(Mixes mixes) {
		this.sql=mixes.sql();
		this.entityClass=mixes.entity();
		this.primaryKey=mixes.primaryKey();
		this.immediate=!mixes.lazy();
		if(this.immediate) {//设置主映射存在实时混合
			this.mainMapping.hasImmediateMixes(true);
		}
	}

	@Override
	protected void initProperties() {
		Set<PropertyDescriptor> writers=mixBeanInfo.getWritePropertyDescriptors();
		for (PropertyDescriptor pd : writers) {
			try {
				Property property=AnnotationUtils.getAnnotation(pd.getWriteMethod(), Property.class);
				if (property != null) {
					initProperty(property,pd);
				} 
			} catch (Exception e) {
				throw new OrmInitException("初始化 OrmObjMapper 失败："+rawClass,e);
			}
		}
	}
	
	protected void initProperty(Property p,PropertyDescriptor pd) {
		OrmBeanProperty property=this.newOrmProperty(pd);
		String column=p.column();
		if(StringUtils.isEmpty(column)) {
			column=HumpKeyColumnSwaper.instance.KeyToColumn(pd.getName());
		}
		property.setColumnName(column);
		property.setPropertyName(pd.getName());
		propertyMap.put(property.getPropertyName(), property);
		columnMap.put(property.getColumnName(), property);
		property.setPersistence(true);
		initNote(pd.getWriteMethod(), property);
	}
	/***
	 * 加载数据到映射对象中
	 * @param targetBean
	 */
	public void loadDatasToBean(V targetBean) {
		if(this.entityClass!=NULL.class) {
			OrmProperty idProperty=mainMapping.getProperty(this.primaryKey);
			if(idProperty==null) {
				idProperty=mainMapping.getPropertyByColumn(this.primaryKey);
			}
			Object primaryKeyValue=idProperty.readValue(targetBean);
			if(primaryKeyValue!=null) {
				//以实体方式加载
				this.daoSupport.queryMixTo(this.entityClass, primaryKeyValue,this,targetBean);
			}
			
		}else if(StringUtils.notEmpty(this.sql)) {
			BoundSql boundSql=new NamedBoundSql(sql, targetBean);
			this.daoSupport.queryMixTo(boundSql, this, targetBean);
			
		}
	}

	public void setDaoSupport(EnableSupportDAO daoSupport) {
		this.daoSupport = daoSupport;
	}

	/**
	 * 是否即时加载
	 * @return
	 */
	public boolean isImmediate() {
		return immediate;
	}
	
	

}
