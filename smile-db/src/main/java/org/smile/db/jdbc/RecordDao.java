package org.smile.db.jdbc;

import org.smile.db.PageModel;
import org.smile.db.handler.ResultSetMap;
import org.smile.db.handler.RowHandler;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface RecordDao<E> extends LambdaRecordDao<E>{

	/**
	 * 查询数据
	 * @param whereSql 条件语句
	 * @param params 条件语句占位符参数
	 * @return
	 * @throws SQLException
	 */
	public  List<E> query(String whereSql, Object... params);
	
	/**
	 * 以条件语句删除
	 * 
	 * @param sqlWhere
	 * @param params
	 * @throws SQLException
	 */
	public int delete(String sqlWhere, Object... params);


	/**
	 * 更新一个对象到数据库中
	 * 
	 * @param e 必须指定一个id
	 * @param fieldName 属性字段
	 * @throws SQLException
	 */
	public int update(E e, String[] fieldName);
	
	/**
	 * 更新一个对象到数据库中
	 * 
	 * @param list 其中的元素必须指定一个id
	 * @param fieldName 属性字段
	 * @throws SQLException
	 */
	public int[] updateBatch(List<E> list, String[] fieldName);

	/**
	 * 分页查询
	 * @param page
	 * @param size
	 * @param whereSql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public PageModel<E>  queryPage(int page, int size, String whereSql, Object[] params);
	/**
	 * 分页查询
	 * @param page
	 * @param size
	 * @param whereSql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public <T> PageModel<T>  queryPage(int page, int size,RowHandler rowHandler, String whereSql, Object[] params);
	/**
	 * 查询唯一值 如果返回结果不是唯一记录会抛出异常
	 * @param whereSql
	 * @param params
	 * @return
	 * @throws SQLException 结果不是唯一时候异常
	 */
	public E queryUnique(String whereSql, Object... params);
	/**
	 * 	返回自定义实现的类型
	 * @param <T>
	 * @param rowHandler 行转换操作
	 * @param whereSql
	 * @param params
	 * @return
	 */
	public <T> List<T> query(RowHandler rowHandler,String whereSql,Object... params);
	/**
	 * 自定义转换第一条
	 * @param <T>
	 * @param rowHandler
	 * @param whereSql
	 * @param params
	 * @return
	 */
	public <T> T queryFirst(RowHandler rowHandler,String whereSql,Object... params);
	/**
	 * 查询第一个对象
	 * @param whereSql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public  E queryFirst(String whereSql,Object... params);
	/***
	 * 执行一个sql语句，返回对对象封装的列表
	 * @param sql 要查询的语句
	 * @param params ?参数
	 * @return
	 * @throws SQLException
	 */
	public List<E> querySql(String sql,Object ... params);

	/**
	 * 加载对象
	 * @param obj 主键不能为空
	 * @throws SQLException
	 */
	public void load(Object obj,String... propertyNames);
	/**
	 * 查询数据条数
	 * @param whereSql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public long queryCount(String whereSql, Object... params);


	/**
	 * 更新部分字段 以where条件更新
	 * @param fieldNames
	 * @param namedWhereSql 要更新的where条件
	 * @param params
	 * @return
	 */
	public int update(String[] fieldNames,String namedWhereSql,Map<String,Object> params);

	/**
	 *      限制条数语句
	 * @param whereSql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public List<E> queryLimit(long offset,int limit,String whereSql,Object... params );
	
	/**
	 *      限制条数语句
	 * @param whereSql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public <T> List<T> queryLimit(long offset,int limit,RowHandler rowHandler,String whereSql,Object... params );

	/**
	 * 以其它类型对象返回
	 * @param <T>
	 * @param offset
	 * @param limit
	 * @param whereSql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public <T> List<T> queryLimitObjectList(long offset,int limit,String[] queryFields,Class<T> resClass,String whereSql,Object...params );
	/**
	 * @param <T>
	 * @param offset
	 * @param limit
	 * @param whereSql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public <T> List<T> queryLimitMapList(long offset,int limit,String[] queryFields,String whereSql,Object...params );
	/**
	 * 查询限制条数语句
	 * @param <T>
	 * @param offset
	 * @param limit
	 * @param queryFields 要查询的字段
	 * @param whereSql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public <T> List<T> queryLimitList(long offset,int limit,String[] queryFields,RowHandler rowHandler,String whereSql,Object...params );
	/**
	 * 	查询前多少条数据
	 * @param top
	 * @param whereSql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public List<E> queryTop(int top, String whereSql, Object... params);
	
	/**
	 * 单个字段返回列表
	 * @param resClass 返回单字段类型
	 * @param whereSql 条件语句
	 * @param params 条件参数
	 * @return
	 * @throws SQLException
	 */
	public <T> List<T> queryOneFieldList(String field,Class<T> resClass,String whereSql,Object... params);
	/**
	 * 查询间个字段
	 * @param <T>
	 * @param resClass 返回类型
	 * @param whereSql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public <T> T queryOneField(String field,Class<T> resClass,String whereSql,Object... params);
	/**
	 * 单个字段返回int
	 * @param whereSql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public Integer queryForInt(String field,String whereSql,Object...params );
	/**
	 * 单个字段返回long
	 * @param whereSql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public Long queryForLong(String field,String whereSql,Object...params );
	/**
	 * 单个字段返回字符串
	 * @param whereSql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public String queryForString(String field,String whereSql,Object...params );
	
	/**
	 * 单个字段返回double
	 * @param whereSql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public Double queryForDouble(String field,String whereSql,Object...params );
	
	/**
	 * 单个字段返回double
	 * @param whereSql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public List<String> queryForStringList(String field,String whereSql,Object...params );
	/**
	 * 查询返回列表,用其它对象封装
	 * @param <T>
	 * @param res 用于封装的类
	 * @param whereSql 条件语句
	 * @param params 条件参数
	 * @return
	 * @throws SQLException
	 */
	public <T> List<T> queryForObjectList(String[] fields,Class<T> res,String whereSql,Object... params);
	/**
	 * 查询返回一个对象 此对象不是当前实体映射时使用
	 * @param <T>
	 * @param res 返回对象类型
	 * @param whereSql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public <T> T queryForObject(String[] fields,Class<T> res,String whereSql,Object... params);
	/**
	 * 查询指定字段返回map
	 * @param fields
	 * @param whereSql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public List<ResultSetMap> queryForMapList(String[] fields,String whereSql,Object ...params );
	/**
	 *    查询指定字段返回map
	 * @param fields
	 * @param whereSql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public ResultSetMap queryForMap(String[] fields,String whereSql,Object ...params );
	/**
	 * 查询返回以其它类型对象封装
	 * @param <T>
	 * @param res 返回封装类型
	 * @param whereSql
	 * @param params
	 * @return
	 */
	public <T> List<T> queryForObjectList(Class<T> res, String whereSql, Object... params);

	public Class resultClass();

}
