package org.smile.orm.base;

import java.util.List;

import org.smile.db.Db;
import org.smile.orm.ann.Batch;
import org.smile.orm.ann.Delete;
import org.smile.orm.ann.Insert;
import org.smile.orm.ann.Select;
import org.smile.orm.ann.Update;
/***
 * 这是一个基础的数据操作dao接口 
 * 提供为orm配置DAO接口继承
 * @author 胡真山
 * @Date 2016年1月14日
 */
public interface IBaseDao<T> {
	/**
	 * 往数据插入一个对象  
	 * @param obj 注解了table 的类的实例
	 */
	@Insert
	public void insert(T obj);
	/**
	 * 往数据更新一个对象   必须是批定了一个id字段
	 * 是全属性更新
	 * @param obj 注解了table 的类的实例
	 */
	@Update
	public void update(T obj);
	/**
	 * 往数据删除一个对象   必须是批定了一个id字段
	 * 以id字段删除
	 * @param obj 注解了table 的类的实例
	 */
	@Delete
	public void delete(T obj);
	/**
	 * 往数据查询一个对象   必须是批定了一个id字段
	 * @param obj 注解了table 的类的实例
	 */
	@Select
	public T get(T obj);
	/**
	 * 往数据更新多个对象
	 * 是全属性插入
	 * @param objs 注解了table 的类的实例集合
	 */
	@Batch(type=Db.INSERT)
	public void insertBatch(List<T> objs);
	/**
	 * 往数据更新多个对象  必须是批定了一个id字段
	 * 是全属性更新
	 * @param objs 注解了table 的类的实例集合
	 */
	@Batch(type=Db.UPDATE)
	public void updateBatch(List<T> objs);
	/**
	 * 往数据更新多个对象   必须是批定了一个id字段
	 * @param objs 注解了table 的类的实例集合
	 */
	@Batch(type=Db.DELETE)
	public void deleteBatch(List<T> objs);
}
