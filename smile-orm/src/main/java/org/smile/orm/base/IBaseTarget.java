package org.smile.orm.base;

import java.util.List;

import org.smile.log.LoggerHandler;

public interface IBaseTarget extends LoggerHandler{
	/**
	 * 往数据插入一个对象  
	 * @param obj 注解了table 的类的实例
	 */
	public void insert(Object obj) ;
	/**
	 * 往数据更新一个对象   必须是批定了一个id字段
	 * 是全属性更新
	 * @param obj 注解了table 的类的实例
	 */
	public void update(Object obj) ;
	/**
	 * 往数据删除一个对象   必须是批定了一个id字段
	 * 以id字段删除
	 * @param obj 注解了table 的类的实例
	 */
	public void delete(Object obj) ;
	/**
	 * 往数据查询一个对象   必须是批定了一个id字段
	 * @param obj 注解了table 的类的实例
	 */
	public <E> E get(E obj) ;
	/**
	 * 从数据库中加载属性  
	 * @param obj 参数中的源对象
	 * @return
	 * @
	 */
	public <E> E load(E obj,String ... propertyNames) ;
	/**
	 * 往数据库插入多个对象
	 * 是全属性插入
	 * @param objs 注解了table 的类的实例集合
	 */
	public void insertBatch(List objs) ;
	/**
	 * 往数据更新多个对象  必须是批定了一个id字段
	 * 是全属性更新
	 * @param objs 注解了table 的类的实例集合
	 */
	public void updateBatch(List objs) ;
	/**
	 * 往数据库删除多个对象 
	 * 必须是指定了一个id字段的值
	 * @param objs 注解了table 的类的实例集合
	 */
	public void deleteBatch(List objs) ;
}
