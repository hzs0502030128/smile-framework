package org.smile.db.handler;
/**
 * 数据库列和映射的键交换器
 * @author 胡真山
 *
 */
public interface KeyColumnSwaper {
	/**
	 * 由列名转成键名
	 * @param column
	 * @return
	 */
	public String columnToKey(String column);
	/**
	 * 由键名转列名
	 * @param key
	 * @return
	 */
	public String KeyToColumn(String key);
}
