package org.smile.db.handler;

/**
 * 这是一个键会转为驼峰式的map实现
 * 用于对数据库的字段进行封装
 * first_name --> firstName
 * @author 胡真山
 *
 */
public class HumpResultSetMap extends RecordSetMap{
	
	public HumpResultSetMap() {
		this.keyColumnSwaper=HumpKeyColumnSwaper.instance;
	}
}
