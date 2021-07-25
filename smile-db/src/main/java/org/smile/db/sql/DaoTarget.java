package org.smile.db.sql;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.smile.collection.CollectionUtils.GroupKey;
import org.smile.db.DataSourceSupport;
import org.smile.db.PageModel;
import org.smile.db.Transaction;
import org.smile.db.handler.RowHandler;

/**
 * 自定义实现接口方法的实现类 
 * 如果需要一个datasource 必须实现此接口
 * 在实现化的时候会设置一个datasource
 * @author 胡真山
 * 2015年12月10日
 */
public interface DaoTarget extends DataSourceSupport {
	/**
	 * 查询第一条语句
	 * @param <E>
	 * @param boundSql
	 * @param rowHandler
	 * @return
	 * @throws SQLException
	 */
	public <E> E queryFirst(BoundSql boundSql, RowHandler rowHandler);

	/**
	 * @param boundSql 查询参数封装
	 * @param rowHandler 数据行转换器
	 * @return
	 * @throws SQLException
	 */
	public <E> List<E> query(BoundSql boundSql, RowHandler rowHandler);

	/**
	 * 行数限制查询
	 * @param <E>
	 * @param boundSql
	 * @param rowHandler
	 * @param offset
	 * @param limit
	 * @return
	 * @throws SQLException
	 */
	public <E> List<E> queryLimit(BoundSql boundSql, RowHandler rowHandler, long offset, int limit);

	/**
	 * 
	 * @param boundSql 查询语句封装
	 * @param rowHandler 数据行转换器
	 * @param groupKey 结果集分组
	 * @return
	 * @throws SQLException
	 */
	public <K, E> Map<K, E> queryForKeyMap(BoundSql boundSql, RowHandler rowHandler, GroupKey<K, E> groupKey);

	/**
	 * 返回分组Map
	 * @param boundSql
	 * @param rowHandler
	 * @param groupKey
	 * @return
	 * @throws SQLException
	 */
	public <K, E> Map<K, List<E>> queryForGroupMap(BoundSql boundSql, RowHandler rowHandler, GroupKey<K, E> groupKey);

	/**
	 *  查询前多少条数据
	 * @param boundSql 查询参数封装
	 * @param rowHandler 数据行转换器
	 * @return
	 * @throws SQLException
	 */
	public <E> List<E> queryTop(int top, BoundSql boundSql, RowHandler rowHandler);

	/**
	 * 分页查询对象
	 * 
	 * @param c 返回对象的封装类型
	 * @param whereSql 过滤条件
	 * @return
	 * @throws Exception
	 */
	public <E> PageModel<E> queryPageSql(RowHandler rowHandler, BoundSql boundSql, int page, int size);

	/**
	 * 初始化事务控制
	 * @return
	 * @throws SQLException
	 */
	public Transaction initTransaction();
	/**
	 * 操作结束时对事务的处理
	 * @param transaction
	 * @throws SQLException
	 */
	public void endTransaction(Transaction transaction);
	
}
