package org.smile.db.sql;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.smile.collection.CollectionUtils.GroupKey;
import org.smile.dataset.DataSet;
import org.smile.db.PageModel;
/**
 * jdbc sql语句执行
 * @author 胡真山
 *
 */
public interface SqlExecutor {
	/**
	 * 
	 * 执行一条sql语句 
	 * @param sql 要执行的sql语句   可以是存在 ? 占位的语句
	 * @param params 要赋值占位?的参数 的一个数组
	 * @return 
	 * @throws Exception 
	 */
	public abstract int executeUpdate(String sql, Object... params) throws SQLException;
	/**
	 * 执行一条更新语句
	 * @param boundSql
	 * @return
	 * @throws SQLException
	 */
	public abstract int executeUpdate(BoundSql boundSql) throws SQLException;

	/**
	 * 查询一条sql语句
	 *   list中的对象类型由handler对象决定 
	 *   如果不指定handler 将使用默认的MapRowHandler返回的是一个Map的List
	 * 查询一条语句
	 * @param sql
	 * @param params
	 * @return 
	 * @throws SQLException 
	 * @throws Exception
	 */
	public abstract <E> List<E> query(String sql, Object... params) throws SQLException;

	/**
	 * 查询唯一行数据
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public abstract <E> E queryUnique(String sql, Object... params) throws SQLException;

	/**
	 * 查询 返回列表
	 * @param boundSql
	 * @return
	 * @throws SQLException
	 */
	public abstract <E> List<E> query(BoundSql boundSql) throws SQLException;

	/**
	 * 执行一条sql语句
	 * @param sql 
	 * @return
	 * @throws SQLException
	 */
	public abstract boolean execute(String sql, Object... params) throws SQLException;

	/**
	 * 插入操作
	 * @param boundSql 
	 * @return 返回自动增加的字段的值
	 * @throws SQLException
	 */
	public abstract Object insertAtuoincrement(BoundSql boundSql) throws SQLException;

	/***
	 * 执行Sql操作
	 * @param boundSql
	 * @return
	 * @throws SQLException
	 */
	public abstract boolean execute(BoundSql boundSql) throws SQLException;

	/**
	 * 
	 * @param sql SELECT * FROM T WHERE name=:name
	 * @param params {name:胡真山}
	 * @return 
	 * @throws SQLException
	 */
	public  boolean execute(String sql, Map<String, Object> params) throws SQLException;

	/**
	 * 支持top 分页的数据库分布查询 
	 * 如果是sql2000以版本 不推荐使用此 方法
	 * @param sql
	 * @param page
	 * @param size
	 * @return
	 * @throws SQLException 
	 */
	public  <E> PageModel<E> queryTopPageSql(String sql, Object[] params, int page, int size) throws SQLException;

	/**
	 * 只支持top关键字分页的数据库使用此方法分页查询
	 * 此方法只使用到一个top把所有top后的查询出来
	 * 再用游标取相应的条数
	 * @param boundSql
	 * @param page
	 * @param size
	 * @return
	 * @throws SQLException
	 */
	public  <E> PageModel<E> queryTopPageSql(BoundSql boundSql, int page, int size) throws SQLException;

	/**
	 * 适用于只能用top的数据库
	 * @param sql
	 * @param params
	 * @param page
	 * @param size
	 * @return
	 * @throws SQLException
	 */
	public  <E> PageModel<E> queryTopPageSql(String sql, Map params, int page, int size) throws SQLException;

	/**
	 * 批量执行sql
	 * @param conn
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public  int[] batch(String sql, Object[][] params) throws SQLException;

	/**
	  * 批量操作
	  * @param boundSql
	  * @param list 用于批量操作的赋值列表
	  * @return
	  * @throws SQLException
	  */
	public  int[] batch(BoundSql boundSql, Collection list) throws SQLException;
	/**
	 * 批量执行sql
	 * @param conn
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public  int[] batch(BoundSql boundSql, Object[] params) throws SQLException;
	/**
	 * 批量执行sql语句
	 * @param sqls  一个sql语句的集合 
	 * @return
	 * @throws SQLException
	 */
	public  int[] batch(Collection<String> sqls) throws SQLException;

	/**
	 * 执行一条sql语句
	 */
	public  int executeUpdate(String sql, Map<String, Object> params) throws Exception;

	/**
	 * 查询结果为单对象 适用于只有结果只有一行数据的 对象的类型由RowHanndler决定
	 * 可能是一个bean 、list ,array ,map
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public  <E> E queryFirst(String sql, Object... params) throws SQLException;
	/**
	 * 查询结果为单对象 适用于只有结果只有一行数据的 对象的类型由RowHanndler决定
	 * 可能是一个bean 、list ,array ,map
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public  <E> E queryForObject(BoundSql boundSql) throws SQLException;
	
	/**
	 * 只查询 第一条数据
	 * @param boundSql
	 * @return
	 * @throws SQLException
	 */
	public <E> E queryFirst(BoundSql boundSql) throws SQLException;
	
	public <E> E queryUnique(BoundSql boundSql) throws SQLException ;

	/**
	 * 分页查询数据
	 * @param countSql 总条数语句
	 * @param dataSql 数据语句
	 * @param page
	 * @param size
	 * @return
	 */
	public  <E> PageModel<E> queryPageSql(BoundSql boundSql, int page, int size) throws SQLException;
	/**
	 * 查询前N条数据
	 * @param sql 数据语句
	 * @param size
	 * @return
	 */
	public  <E> List<E> queryTop(BoundSql boundSql,int size) throws SQLException;
	/**
	 * 分页查询数据
	 * @param countSql
	 * @param dataSql
	 * @param page
	 * @param size
	 * @return
	 */
	public  <E> PageModel<E> queryPageSql(String sql, int page, int size, Object... params) throws SQLException;

	/**
	 * 分页查询
	 * @param sql select * from t where name=:name
	 * @param params {name:test}
	 * @param page
	 * @param size
	 * @return
	 * @throws SQLException
	 */
	public  <E> PageModel<E> queryPageSql(String sql, Map params, int page, int size) throws SQLException;
	/**
	 * 更新返回结果为一个分组的Map
	 * @param boundSql
	 * @param groupKey
	 * @return
	 * @throws SQLException
	 */
	public <K,E> Map<K,List<E>> queryForGroupMap(BoundSql boundSql,GroupKey<K, E> groupKey) throws SQLException;
	/**
	 * 查询
	 * @param boundSql
	 * @return
	 * @throws SQLException
	 */
	public DataSet queryDataSet(BoundSql boundSql) throws SQLException;
	/**
	 * 查询limit数量
	 * @param boundSql
	 * @param offset 偏移行数
	 * @param limit 要查询的数据条数
	 * @return
	 * @throws SQLException
	 */
	public <E> List<E> queryLimitSql(BoundSql boundSql, long offset, int limit) throws SQLException;
}