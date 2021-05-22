package org.smile.orm.mapping.property;

import org.smile.commons.IdGenerator;
import org.smile.db.sql.Mapping;
import org.smile.orm.OrmInitException;
import org.smile.reflect.ClassTypeUtils;
/**
 * 主键属性封装
 * @author 胡真山
 * @Date 2016年2月25日
 */
public class OrmIdProperty implements Mapping{
	/**是否是uuid类型支持的*/
	private boolean isUuid=false;
	/**对应的字段属性 */
	private OrmProperty property;
	/**是否是基本类型*/
	private boolean isBasicType=false;
	/**ID的默认值*/
	private Object defaultValue;
	/**主键ID生成器*/
	private IdGenerator idGenerator;
	
	public OrmIdProperty(OrmProperty property){
		this.property=property;
		isBasicType=ClassTypeUtils.isBasicType(property.getFieldType());
		defaultValue=ClassTypeUtils.basicNullDefault(property.getFieldType());
	}
	
	public boolean isUuid() {
		return isUuid;
	}
	
	public boolean hasIdGenerator() {
		return this.idGenerator!=null;
	}
	
	public void setUuid(boolean isUuid) {
		this.isUuid = isUuid;
	}
	
	public OrmProperty getProperty() {
		return property;
	}
	
	public void setProperty(OrmProperty property) {
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
	
	/**
	 * 是否是自动增长的
	 * @return
	 */
	public boolean isAtuoincrement(){
		return property.isAtuoincrement();
	}
	/**
	 * 获取对象的主键的值
	 * @param target
	 * @return
	 */
	public Object getKeyValue(Object target){
		return property.readValue(target);
	}
	/**
	 * 设置对象的主键的值
	 * @param target
	 * @param value
	 */
	public void setKeyValue(Object target,Object value){
		property.writeValue(target, value);
	}
	/**
	 * 是否是基本类型的默认值
	 * @param value
	 * @return
	 */
	public boolean isBasciDefaultValue(Object value){
		if(isBasicType){
			return value.equals(this.defaultValue);
		}
		return false;
	}
	/**
	 * 生成ID
	 * @return
	 */
	public Object generateId() {
		if(this.idGenerator!=null) {
			return idGenerator.generate();
		}
		throw new OrmInitException("not exits id generator");
	}

	public void setIdGenerator(IdGenerator idGenerator) {
		this.idGenerator = idGenerator;
	}
	
	
}
