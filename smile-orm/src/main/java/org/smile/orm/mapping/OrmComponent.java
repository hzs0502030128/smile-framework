package org.smile.orm.mapping;

import java.lang.reflect.Field;
import java.util.Map;

import org.smile.orm.OrmInitException;
import org.smile.orm.mapping.flag.PropertyFlag;
import org.smile.orm.mapping.property.OrmFieldProperty;
import org.smile.reflect.FieldUtils;

/**
 * orm的组件
 *	以组件的形式装配到实体上
 * @author 胡真山
 *
 */
public class OrmComponent<M,V> extends OrmMapping<V>{
	/**
	 * 主映射
	 */
	private OrmObjMapping<M> mainMapping;
	/**
	 * 此组件在主映射上的的字段
	 */
	private Field field;
	
	public OrmComponent(OrmObjMapping<M> mainMapping,Field field) {
		this.mainMapping=mainMapping;
		this.field=field;
	}

	@Override
	public void initType(Class clazz) throws MappingException {
		this.rawClass = clazz;
		this.name=field.getName();
		initProperties();
	}

	@Override
	protected void initProperties() {
		Map<String,Field> fields = FieldUtils.getAnyNoStaticField(rawClass);
		for (Field field : fields.values()) {
			try {
				PropertyFlag propertyFlag = flagHandler.getPropertyFlag(tableFlag,field);
				if (propertyFlag != null) {
					OrmFieldProperty property=propertyFlag.getProperty(mainMapping);
					propertyMap.put(property.getPropertyName(), property);
					columnMap.put(property.getColumnName(), property);
					property.setPersistence(true);
					property.setComponent(this);
					initNote(field, property);
				} 
			} catch (Exception e) {
				throw new OrmInitException("初始化 OrmObjMapper 失败："+rawClass,e);
			}
		}
	}
}
