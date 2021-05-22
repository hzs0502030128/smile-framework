package org.smile.orm.base;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.smile.collection.CollectionUtils.GroupKey;

public interface EnableSupportDAO extends BaseDAO{
	/**
	 * 按对象禁用数据
	 * @param id
	 * @
	 */
	public void disable(Object obj) ;
	/**
	 * 启用数据按对象
	 * @param id
	 * @
	 */
	public void enable(Object obj) ;
	/**
	 * 启用全部
	 * @param clazz
	 * @
	 */
	public void enableAll(Class clazz) ;
	/**
	 * 全不启用
	 * @param clazz
	 * @
	 */
	public void disableAll(Class clazz) ;
	/**
	 * 按where条件不启用
	 * @param clazz
	 * @param where
	 * @param params
	 * @
	 */
	public void disable(Class clazz ,String where,Object ... params) ;
	/**
	 * 按where条件启用
	 * @param clazz
	 * @param where
	 * @param params
	 * @
	 */
	public void enable(Class clazz ,String where,Object ... params) ;
	/**
	 * 按id 不启用
	 * @param clazz
	 * @param ids
	 * @
	 */
	public void disableByIds(Class clazz, Collection ids) ;
	/**
	 * 按id启用
	 * @param clazz
	 * @param ids
	 * @
	 */
	public void enableByIds(Class clazz, Collection ids) ;
	/**
	 * 批量禁用
	 * @param list
	 * @
	 */
	public void disableBatch(List list) ;
	/**
	 * 批量启用
	 * @param list
	 * @
	 */
	public void enableBatch(List list) ;
	/**
	 * 查询结果生成一个分组的Map
	 * @param c
	 * @param whereSql
	 * @param groupKey
	 * @param params
	 * @return
	 * @
	 */
	public <K,E> Map<K,List<E>> queryForGroupMap(Class<E> c,String whereSql,GroupKey<K, E> groupKey,Object ...params) ;
		
}
