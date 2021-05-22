package org.smile.db.criteria;

import org.smile.db.PageModel;
import org.smile.db.handler.ResultSetMap;

import java.sql.SQLException;
import java.util.List;

public interface BaseCriteria<E> {

	/***
	 * 设置分页偏移行数
	 * @param offset
	 * @return
	 */
	public Criteria<E> offset(long offset);
	/**
	 * 设置限制查询行数
	 * @param limit
	 * @return
	 */
	public Criteria<E> limit(int limit);
	/**
	 * 添加一个查询条件 使用and连接
	 * @param criterion
	 * @return
	 */
	public Criteria<E> and(Criterion criterion);

	/**
	 * 新增加一个查询条件 使用or连接
	 * @param criterion
	 * @return
	 */
	public Criteria<E> or(Criterion criterion);


	/**
	 * 查询操作
	 * @return
	 * @throws SQLException
	 */
	public List<E> list();
	/**
	 * 删除操作
	 * @return
	 * @throws SQLException
	 */
	public int delete();
	/**
	 *	查询数据条数
	 * @return
	 * @throws SQLException
	 */
	public long count();
	/**
	 * 查询第一个对象
	 * @return
	 * @throws SQLException
	 */
	public E first();
	/**
	 *      分页查询
	 * @param page
	 * @param size
	 * @return
	 * @throws SQLException
	 */
	public PageModel<E> queryPage(int page, int size);
	
	/**
	 * 查询一个表的所有数据
	 * @return
	 * @throws SQLException
	 */
	public  List<E> queryAll();
	
	/**
	 * 删除所有数据
	 * 
	 * @throws SQLException
	 */
	public int deleteAll();
	
	/**
	 * 查询唯一值 如果返回结果不是唯一记录会抛出异常
	 * @return
	 * @throws SQLException 结果不是唯一时候异常
	 */
	public E uinque();
	/**
	 * 	查询前多少条
	 * @param top
	 * @return
	 * @throws SQLException
	 */
	public List<E> top(int top);
	
	/**
	 * 查询单个字段
	 * @param <T>
	 * @param resClass 返回类型
	 * @return SQLException
	 */
	public <T> T forField(Class<T> resClass);
	/**
	 * 单个字段返回int
	 * @return
	 * @throws SQLException
	 */
	public Integer forInt();
	/**
	 * 单个字段返回long
	 * @return
	 * @throws SQLException
	 */
	public Long forLong();
	/**
	 * 单个字段返回字符串
	 * @return
	 * @throws SQLException
	 */
	public String forString();
	
	/**
	 * 单个字段返回double
	 * @return
	 * @throws SQLException
	 */
	public Double forDouble();
	
	/**
	 * 单个字段返回double
	 * @return
	 * @throws SQLException
	 */
	public List<String> listString();
	/**
	 * 查询指定字段返回map
	 * @return
	 * @throws SQLException
	 */
	public List<ResultSetMap> listMap();
	/**
	 * 查询返回列表,用其它对象封装
	 * @param <T>
	 * @return
	 * @throws SQLException
	 */
	public <T> List<T> listObject(Class<T> res);
	/**
	 * 单个字段返回列表
	 * @param resClass 返回单字段类型
	 * @return
	 * @throws SQLException
	 */
	public <T> List<T> listField(Class<T> resClass);
	/**
	 * 查询返回一个对象 此对象不是当前实体映射时使用
	 * @param <T>
	 * @param res 返回对象类型
	 * @return
	 * @throws SQLException
	 */
	public <T> T forObject(Class<T> res);
	
	/**
	 *    查询指定字段返回map
	 * @return
	 * @throws SQLException
	 */
	public ResultSetMap forMap();
	/**
	 * 重置查询信息
	 * @return
	 */
	public Criteria<E> reset();

	
}
