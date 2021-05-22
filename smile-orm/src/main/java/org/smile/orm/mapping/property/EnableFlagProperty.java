package org.smile.orm.mapping.property;

import org.smile.beans.converter.BasicConverter;
import org.smile.beans.converter.ConvertException;
import org.smile.db.sql.Mapping;
/**
 * 数据有效标记的属性字段封装
 * @author 胡真山
 * @Date 2016年2月25日
 */
public class EnableFlagProperty implements Mapping{
	/**
	 * 启用时的值
	 */
	private Object enable;
	/**
	 * 不启用时的值
	 */
	private Object disable;
	/**字段属性*/
	private OrmFieldProperty property;
	
	public EnableFlagProperty(OrmFieldProperty property){
		this.property=property;
	}
	
	/**
	 * 初妈化物料删除的对应值
	 * @param enableValue 有效时的值
	 * @param disableValue 无效时的值
	 * @throws ConvertException
	 */
	public void initFlagValue(String enableValue,String disableValue) throws ConvertException{
		Class<?> fieldType=property.getFieldType();
		this.enable=BasicConverter.getInstance().convert(fieldType,enableValue);
		this.disable=BasicConverter.getInstance().convert(fieldType,disableValue);
	}
	
	public OrmFieldProperty getProperty() {
		return property;
	}
	public void setProperty(OrmFieldProperty property) {
		this.property = property;
	}
	@Override
	public String getPropertyName() {
		return property.getPropertyName();
	}
	@Override
	public String getColumnName() {
		return property.getColumnName();
	}
	@Override
	public String getPropertyExp() {
		return property.getPropertyExp();
	}

	public Object getEnable() {
		return enable;
	}


	public Object getDisable() {
		return disable;
	}

	
}
