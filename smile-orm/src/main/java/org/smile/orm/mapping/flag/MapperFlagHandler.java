package org.smile.orm.mapping.flag;

import java.lang.reflect.Field;
/**
 * 对字段标记处理接口
 * @author 胡真山
 *
 */
public interface MapperFlagHandler {
	/**
	 * 表注解
	 * @param clazz
	 * @return
	 */
	public TableFlag getTableFlag(Class clazz);
	/**
	 * 字段注解
	 * @param field
	 * @return
	 */
	public PropertyFlag getPropertyFlag(TableFlag tableFlag, Field field);
	
}
