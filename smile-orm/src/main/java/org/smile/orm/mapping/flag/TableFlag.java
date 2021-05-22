package org.smile.orm.mapping.flag;

import org.smile.log.LoggerHandler;
/**
 * 类对于表的标记
 * @author 胡真山
 *
 */
public abstract class TableFlag implements LoggerHandler{
	/**
	 * 映射名称
	 */
	protected String name;

	/**
	 * 是否表标记
	 */
	protected boolean flaged = false;
	/**
	 * 映射的类
	 */
	protected Class rawClass;
	/**
	 * 是否是一个表
	 */
	protected boolean isTable;
	/**
	 * 检查标记
	 * @param clazz
	 * @return
	 */
	public abstract boolean checkFlag(Class<?> clazz);
	/**
	 * 是否被标记了
	 * @return
	 */
	public boolean isFlaged() {
		return flaged;
	}

	/**
	 * 映射名称
	 * @return
	 */
	public String getName() {
		return name;
	}
	/**
	 * 映射的类
	 * @return
	 */
	public Class getRawClass() {
		return rawClass;
	}
	
	/**
	 * 是不是一个表映射
	 * @return
	 */
	public boolean isTable(){
		return isTable;
	}

	/**
	 * 是否是以驼峰字段映射
	 * @return
	 */
	public abstract boolean isHumpColumns();
}
