package org.smile.orm.mapping.flag;

import java.lang.reflect.Field;

import org.smile.annotation.AnnotationUtils;
import org.smile.beans.converter.ConvertException;
import org.smile.orm.ann.EnableFlag;
import org.smile.orm.mapping.OrmObjMapping;
import org.smile.orm.mapping.property.EnableFlagProperty;
import org.smile.orm.mapping.property.OrmFieldProperty;
import org.smile.orm.mapping.property.OrmIdProperty;
/**
 * 字段数据库标记标记
 * @author 胡真山
 */
public abstract class PropertyFlag {
	 /**
	  * 列名
	  */
	protected String column;
	/**
	 * 属性名
	 */
	protected String name;
	/**
	 * 是否已经标记
	 */
	protected boolean flaged = false;
	/**
	 * 是否是主键
	 */
	protected boolean primaryKey=false;
	/**
	 * 字段
	 */
	protected Field field;
	/**
	 * 是否是自动增长
	 */
	protected boolean autoincrement=false;
	/**
	 * 验证字段是否是数据为对应标记
	 * @param field
	 * @return
	 */
	public abstract boolean checkFlag(TableFlag tableFlag,Field field);
	/**
	 * 主键配置
	 * @param property
	 * @return
	 */
	public abstract OrmIdProperty getIdProperty(OrmFieldProperty property);
	/**
	 * 字段配置
	 * @param mapper
	 * @return
	 */
	public abstract OrmFieldProperty getProperty(OrmObjMapping mapper);
	
	/**
	 *	 获取失效与启用字段信息
	 * @param property
	 * @return
	 * @throws ConvertException
	 */
	public  EnableFlagProperty getEnableFlagProperty(OrmFieldProperty property) throws ConvertException {
		
		EnableFlagProperty enableProperty=null;
		/**enable 注解*/
		EnableFlag enableFlagAnn= AnnotationUtils.getAnnotation(field,EnableFlag.class);
		if(enableFlagAnn!=null){
			enableProperty=new EnableFlagProperty(property);
			enableProperty.initFlagValue(enableFlagAnn.enable(), enableFlagAnn.disable());
		}
		return enableProperty;
	}
	
	
	/**
	 * 列名
	 * @return
	 */
	public String getColumn() {
		return column;
	}

	public String getName() {
		return name;
	}
	/**
	 * 是否标记了数据库对应
	 * @return
	 */
	public boolean isFlaged() {
		return flaged;
	}

	public boolean isPrimaryKey() {
		return primaryKey;
	}

	public Field getField() {
		return field;
	}
	
}
