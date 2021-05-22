package org.smile.orm.base;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.smile.collection.CollectionUtils.GroupKey;
import org.smile.db.PageModel;
import org.smile.db.handler.ResultSetMap;
import org.smile.db.handler.RowHandler;
import org.smile.db.sql.BoundSql;
import org.smile.db.sql.DaoTarget;
import org.smile.orm.mapping.OrmMapping;

/***
 * 数据据操作基础dao类
 * 
 * @author 胡真山
 */
public interface BaseDAO extends IBaseTarget, DaoTarget{
	/**
	 * 查询一个表的所有数据
	 * @param c
	 * @return
	 * @
	 */
	public <E> List<E> queryAll(Class<E> c) ;

	/**
	 * 查询数据
	 * @param c 映射的对象类
	 * @param whereSql 条件语句
	 * @param params 条件语句占位符参数
	 * @return
	 * @
	 */
	public <E> List<E> query(Class<E> c, String whereSql, Object... params) ;

	/**
	 * 以多个id查询数据
	 * 
	 * @param c 映射类
	 * @param id id的数组
	 * @return 查询出的数据封闭成的对象的列表
	 * @
	 */
	public <E> List<E> queryByIds(Class<E> c, Object... id) ;
	

	/**
	 * 查询数据条数
	 * @param c
	 * @param whereSql
	 * @param params
	 * @return
	 * @
	 */
	public long queryCount(Class c,String whereSql, Object... params) ;
	/**
	 * 以id查询一条数据
	 * @param c 映射的类
	 * @param id 要查询数据的主键
	 * @return 查询出的一个对象
	 * @
	 */
	public <E> E queryById(Class<E> c, Object id) ;

	/**
	 * 以id删除
	 * 
	 * @param c
	 * @param id
	 * @
	 */
	public int deleteById(Class c, Object id) ;
	/***
	 * 按id批量删除
	 * @param c
	 * @param ids
	 * @return
	 * @
	 */
	public int[] deleteByIds(Class c,Collection ids) ;

	/**
	 * 以条件语句删除
	 * 
	 * @param c
	 * @param sqlWhere
	 * @param params
	 * @
	 */
	public int delete(Class c, String sqlWhere, Object... params) ;

	/**
	 * 删除所有数据
	 * 
	 * @param c
	 * @
	 */
	public int deleteAll(Class c) ;

	/**
	 * 添加一条数据
	 * 
	 * @param obj
	 * @
	 */
	public void add(Object obj) ;
	/**
	 * 插入数据到一个映射类的表中
	 * @param c
	 * @param obj 从此对象中解析字段的值
	 * @
	 */
	public void add(Class c,Object obj) ;
	/**
	 * 批量插入数据到一个映射类的表中
	 * @param c 要插入的映射表
	 * @param objs 从此对象中解析字段的值  
	 * @
	 */
	public void add(Class c,Collection objs) ;
	/**
	 * 添加多条为数据
	 * 
	 * @param c
	 * @
	 */
	public void add(List c) ;
	/**
	 * 更新一个对象到数据库中
	 * 
	 * @param e 必须指定一个id
	 * @param fieldname 属性字段
	 * @
	 */
	public int update(Object e, String[] fieldname) ;
	
	/**
	 * 更新一个列表中的所有对象 批量更新
	 * @param objList
	 * @param fieldname
	 * @return
	 * @
	 */
	public void update(List objList, String[] fieldname) ;
	/**
	 * 更新一个对象到数据库中
	 * 
	 * @param obj 不一定是class类的实例对象  必须指定一个id
	 * @param propertyNames 属性字段
	 * @
	 */
	public int update(Class clazz,Object updateObj, String... propertyNames) ;
	/**
	 * 更新一个对象到数据库中
	 * 
	 * @param e 必须指定一个id
	 * @param fieldname 属性字段
	 * @
	 */
	public int[] updateBatch(List list, String[] fieldname) ;
	/**
	 * 批量按id删除
	 * @param c
	 * @param ids
	 * @return
	 * @
	 */
	public int[] deleteByIds(Class c, Object[] ids) ;
	/**
	 * 分页查询
	 * @param clazz
	 * @param page
	 * @param size
	 * @param whereSql
	 * @param params
	 * @return
	 * @
	 */
	public <E> PageModel<E>  queryPage(Class<E> clazz, int page, int size, String whereSql, Object... params) ;
	/**
	 * 查询唯一值 如果返回结果不是唯一记录会抛出异常
	 * @param c
	 * @param whereSql
	 * @param params
	 * @return
	 * @ 结果不是唯一时候异常
	 */
	public <E> E queryUinque(Class<E> c, String whereSql, Object... params) ;
	/**
	 * 查询第一个对象
	 * @param c
	 * @param whereSql
	 * @param params
	 * @return
	 * @
	 */
	public <E> E queryFirst(Class<E> c ,String whereSql,Object... params) ;
	/***
	 * 执行一个sql语句，返回对对象封装的列表
	 * @param c 返回的orm类 非必须是table的映射
	 * @param sql 要查询的语句
	 * @param params ?参数
	 * @return
	 * @
	 */
	public <E> List<E> querySql(Class<E> c,String sql,Object ... params) ;

	/**
	 *     以结果对象的一个属性值为键转成Map返回
	 * @param c 不一定必须是table的映射 mapper也可以
	 * @param sql 查询的完整sql语句 此语句是一个可以完成查询的语句
	 * @param groupKey
	 * @param params
	 * @return
	 * @
	 */
	public <K,E> Map<K, E> querySqlForKeyMap(Class<E> c, String sql, GroupKey<K, E> groupKey, Object... params) ;

	/**
	 * 以结果对象的一个属性值为键转成Map返回
	 * @param c 必须是一个table映射  需要从此类生成查询语句
	 * @param whereSql 只是条件语句
	 * @param groupKey
	 * @param params
	 * @return
	 * @
	 */
	public <K,E> Map<K, E> queryForKeyMap(Class<E> c, String whereSql, GroupKey<K, E> groupKey, Object... params) ;
	/**
	 * 插入或保存  以主键判断
	 * @param obj 要处理的对象
	 */
	public void saveOrUpdate(Object obj) ;

	/**
	 *	 查询前top条数据
	 * @param <E>
	 * @param c
	 * @param top 前面多少数数据
	 * @param whereSql
	 * @param params
	 * @return
	 * @
	 */
	public <E> List<E> queryTop(Class<E> c, int top, String whereSql, Object... params) ;
	/**
	 * 单个字段返回列表
	 * @param <E>
	 * @param c 实例类
	 * @param resClass 返回单字段类型
	 * @param whereSql 条件语句
	 * @param params 条件参数
	 * @return
	 * @
	 */
	public <E> List<E> queryOneFieldList(Class c,String field,Class<E> resClass,String whereSql,Object... params) ;
	/**
	 * 查询间个字段
	 * @param <E>
	 * @param c 实例类
	 * @param resClass 返回类型
	 * @param whereSql
	 * @param params
	 * @return
	 * @
	 */
	public <E> E queryOneField(Class c,String field,Class<E> resClass,String whereSql,Object... params) ;
	/**
	 * 单个字段返回int
	 * @param c
	 * @param whereSql
	 * @param params
	 * @return
	 * @
	 */
	public Integer queryForInt(Class c,String field,String whereSql,Object...params ) ;
	/**
	 * 单个字段返回long
	 * @param c
	 * @param whereSql
	 * @param params
	 * @return
	 * @
	 */
	public Long queryForLong(Class c,String field,String whereSql,Object...params ) ;
	/**
	 * 单个字段返回字符串
	 * @param c
	 * @param whereSql
	 * @param params
	 * @return
	 * @
	 */
	public String queryForString(Class c,String field,String whereSql,Object...params ) ;
	
	/**
	 * 单个字段返回double
	 * @param c
	 * @param whereSql
	 * @param params
	 * @return
	 * @
	 */
	public Double queryForDouble(Class c,String field,String whereSql,Object...params ) ;
	
	/**
	 * 单个字段返回double
	 * @param c
	 * @param whereSql
	 * @param params
	 * @return
	 * @
	 */
	public List<String> queryForStringList(Class c,String field,String whereSql,Object...params ) ;
	/**
	 * 查询返回列表,用其它对象封装
	 * @param <E>
	 * @param c
	 * @param res 用于封装的类
	 * @param whereSql 条件语句
	 * @param params 条件参数
	 * @return
	 * @
	 */
	public <E> List<E> queryForObjectList(Class c,String[] fields,Class<E> res,String whereSql,Object... params) ;
	/**
	 * 查询返回一个对象 此对象不是当前实体映射时使用
	 * @param <E>
	 * @param c 实体映射类
	 * @param res 返回对象类型
	 * @param whereSql
	 * @param params
	 * @return
	 * @
	 */
	public <E> E queryForObject(Class c,String[] fields,Class<E> res,String whereSql,Object... params) ;
	/**
	 * 查询指定字段返回map
	 * @param c
	 * @param fields
	 * @param whereSql
	 * @param params
	 * @return
	 * @
	 */
	public List<ResultSetMap> queryForMapList(Class c,String[] fields,String whereSql,Object ...params ) ;
	/**
	 *    查询指定字段返回map
	 * @param c
	 * @param fields
	 * @param whereSql
	 * @param params
	 * @return
	 * @
	 */
	public ResultSetMap queryForMap(Class c,String[] fields,String whereSql,Object ...params ) ;

	/**
	 * @param boundSql 查询参数封装
	 * @param rowHandler 数据行转换器
	 * @return
	 * @
	 */
	public <E> List<E> query(BoundSql boundSql, RowHandler rowHandler) ;
	

	
	/**
	 * 创建查询sql语句
	 * @param c
	 * @param fields 指定查询的字段
	 * @param whereSql
	 * @param params
	 * @return
	 * @
	 */
	public BoundSql createBoundSql(Class c, String[] fields, String whereSql, Object... params) ;

	/**
	 * 创建查询sql语句
	 * @param c
	 * @param whereSql
	 * @param params
	 * @return
	 * @
	 */
	public BoundSql createBoundSql(Class c, String whereSql, Object... params) ;

	/**
	 * 查询混合到一个对象
	 * @param <E>
	 * @param c
	 * @param id
	 * @param mixTarget
	 */
	public <E> void queryMixTo(Class c, Object id, OrmMapping<E> mixMapping, E mixTarget);
	/**
	 * 查询结果混合到一个对象中
	 * @param <E>
	 * @param boundSql
	 * @param mixMapping
	 * @param mixTarget
	 */
	public <E> void queryMixTo(BoundSql boundSql,OrmMapping<E> mixMapping, E mixTarget);
	
}
