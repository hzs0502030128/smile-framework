package org.smile.beans;

public interface PropertiesGetter<K,V> {
	/***
	 * 获取字段属性
	 * @param name
	 * @return
	 */
	public V getValue(K name);
}
