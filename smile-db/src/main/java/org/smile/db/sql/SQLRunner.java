package org.smile.db.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.smile.collection.CollectionUtils;
import org.smile.collection.CollectionUtils.GroupKey;
import org.smile.dataset.ArrayRow;
import org.smile.dataset.BaseDataSet;
import org.smile.dataset.DataSet;
import org.smile.dataset.DataSetMetaData;
import org.smile.dataset.DataSetMetaDataImpl;
import org.smile.db.DbConstans;
import org.smile.db.Dialect;
import org.smile.db.PageModel;
import org.smile.db.SqlRunException;
import org.smile.db.Transaction;
import org.smile.db.Transactionable;
import org.smile.db.handler.RowHandler;
import org.smile.db.result.QueryResult;
import org.smile.db.result.ResultUtils;
import org.smile.db.sql.page.DialectPage;
import org.smile.db.sql.page.SQL2000DialectPage;
import org.smile.db.sql.parameter.ArrayParameterFiller;
import org.smile.db.sql.parameter.ParameterFiller;
import org.smile.json.JSONObject;
import org.smile.log.LoggerHandler;

/**
 * 数据库操作者
 * @author 胡真山
 *
 */
public class SQLRunner implements LoggerHandler, SqlExecutor,Transactionable {
	/**数据库言，默认为*/
	private Dialect dbDialect = DbConstans.DIALECT;
	/** 结果集处理类*/
	private RowHandler handler;
	/**用于事务控制*/
	private Transaction transaction;

	public SQLRunner(Transaction transaction) {
		this.transaction = transaction;
		this.handler = RowHandler.RESULT_SET_MAP;
	}

	/**
	 * 构造方法
	 * @param conn
	 */
	public SQLRunner(Transaction transaction, RowHandler handler) {
		this.transaction = transaction;
		this.handler = handler;
	}

	public SQLRunner() {
		handler = RowHandler.RESULT_SET_MAP;
	}

	/**
	 * 
	 * 执行一条sql语句 
	 * @param sql 
	 * @param params 要设置的参数 的一个数组
	 * @return 
	 * @throws Exception 
	 */
	@Override
	public int executeUpdate(String sql, Object... params) {
		return executeUpdate(new ArrayBoundSql(sql, params));
	}

	@Override
	public int executeUpdate(BoundSql boundSql) {
		PreparedStatement ps = null;
		String sql =null;
		try {
			sql= boundSql.getSql();
			logger.debug(sql);
			ps = transaction.getConnection().prepareStatement(sql);
			boundSql.fillStatement(ps);
			return ps.executeUpdate();
		} catch (Exception e) {
			throw new SqlRunException("执行语句出错:" + sql + "出错,参数" + JSONObject.toJSONString(boundSql.getParams()), e);
		} finally {
			closeStatement(ps);
		}
	}

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
	@Override
	public <E> List<E> query(String sql, Object... params){
		return query(new ArrayBoundSql(sql, params));
	}

	/**
	 * 查询 返回列表
	 * @param boundSql
	 * @return
	 * @throws SQLException
	 */
	@Override
	public <E> List<E> query(BoundSql boundSql){
		PreparedStatement ps = null;
		String sql =null;
		try {
			sql=boundSql.getSql();
			logger.debug(sql);
			ps = transaction.getConnection().prepareStatement(sql);
			boundSql.fillStatement(ps);
			ResultSet rs = ps.executeQuery();
			return this.parseRsToList(rs);
		} catch (Exception e) {
			throw new SqlRunException("执行语句出错:" + sql + "出错,参数：" + boundSql.paramsToString(), e);
		} finally {
			closeStatement(ps);
		}
	}
	
	@Override
	public DataSet queryDataSet(BoundSql boundSql){
		PreparedStatement ps = null;
		String sql =null;
		try {
			sql=boundSql.getSql();
			logger.debug(sql);
			ps = transaction.getConnection().prepareStatement(sql);
			boundSql.fillStatement(ps);
			ResultSet rs = ps.executeQuery();
			return this.parseRsToDataSet(rs);
		} catch (Exception e) {
			throw new SqlRunException("执行语句出错:" + sql + "出错,参数：" + boundSql.paramsToString(), e);
		} finally {
			closeStatement(ps);
		}
	}
	/**
	 * 查询 返回列表
	 * @param boundSql
	 * @return
	 * @throws SQLException
	 */
	@Override
	public <E> E queryUninque(BoundSql boundSql){
		PreparedStatement ps = null;
		String sql = null;
		try {
			sql=boundSql.getSql();
			logger.debug(sql);
			ps = transaction.getConnection().prepareStatement(sql);
			boundSql.fillStatement(ps);
			ResultSet rs = ps.executeQuery();
			E resultObj= this.parseRsFirst(rs);
			if(rs.next()){
				throw new SQLException("result not uninque"); 
			}
			return resultObj;
		} catch (Exception e) {
			throw new SqlRunException("执行语句出错:" + sql + "出错,参数：" + boundSql.paramsToString(), e);
		} finally {
			closeStatement(ps);
		}
	}
	/**
	 * 只查询 第一条数据
	 * @param boundSql
	 * @return
	 * @throws SQLException
	 */
	@Override
	public <E> E queryFirst(BoundSql boundSql){
		PreparedStatement ps = null;
		String sql =null;
		try {
			sql=boundSql.getSql();
			logger.debug(sql);
			ps = transaction.getConnection().prepareStatement(sql);
			boundSql.fillStatement(ps);
			ResultSet rs = ps.executeQuery();
			return this.parseRsFirst(rs);
		} catch (Exception e) {
			throw new SqlRunException("执行语句出错:" + sql + "出错,参数：" + boundSql.paramsToString(), e);
		} finally {
			closeStatement(ps);
		}
	}

	/**
	 * 以一个字段做为键把结果集list转成Map
	 * @param boundSql
	 * @param groupKey
	 * @return
	 * @throws SQLException
	 */
	public <K, E> Map<K, E> queryForKeyMap(BoundSql boundSql, GroupKey<K, E> groupKey){
		PreparedStatement ps = null;
		String sql =null;
		try {
			sql=boundSql.getSql();
			logger.debug(sql);
			ps = transaction.getConnection().prepareStatement(sql);
			boundSql.fillStatement(ps);
			ResultSet rs = ps.executeQuery();
			return this.parseRsToMap(rs, groupKey);
		} catch (Exception e) {
			throw new SqlRunException("执行语句出错:" + sql + "出错,参数：" + boundSql.paramsToString(), e);
		} finally {
			closeStatement(ps);
		}
	}

	@Override
	public <K, E> Map<K, List<E>> queryForGroupMap(BoundSql boundSql, GroupKey<K, E> groupKey){
		PreparedStatement ps = null;
		String sql = boundSql.getSql();
		try {
			logger.debug(sql);
			ps = transaction.getConnection().prepareStatement(sql);
			boundSql.fillStatement(ps);
			ResultSet rs = ps.executeQuery();
			return this.parseRsToGroupMap(rs, groupKey);
		} catch (Exception e) {
			throw new SqlRunException("执行语句出错:" + sql + "出错,参数：" + boundSql.paramsToString(), e);
		} finally {
			closeStatement(ps);
		}
	}

	/**
	 * 执行一条sql语句
	 * @param sql 
	 * @return
	 * @throws SQLException
	 */
	@Override
	public boolean execute(String sql, Object... params){
		return execute(new ArrayBoundSql(sql, params));
	}

	/**
	 * 插入操作
	 * @param boundSql 
	 * @return 返回自动增加的字段的值
	 * @throws SQLException
	 */
	@Override
	public Object insertAtuoincrement(BoundSql boundSql){
		PreparedStatement ps = null;
		String sql = boundSql.getSql();
		try {
			logger.debug(sql);
			ps = transaction.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			boundSql.fillStatement(ps);
			ps.executeUpdate();
			return ResultUtils.getGeneratedKey(ps);
		} catch (SQLException e) {
			throw new SqlRunException("执行语句出错:" + sql + "出错，参数：" + boundSql.paramsToString() + e.getMessage(), e);
		} finally {
			closeStatement(ps);
		}
	}

	/***
	 * 执行Sql操作
	 * @param boundSql
	 * @return
	 * @throws SQLException
	 */
	@Override
	public boolean execute(BoundSql boundSql) {
		PreparedStatement ps = null;
		String sql = boundSql.getSql();
		try {
			logger.debug(sql);
			ps = transaction.getConnection().prepareStatement(sql);
			boundSql.fillStatement(ps);
			return ps.execute();
		} catch (SQLException e) {
			throw new SqlRunException("执行语句出错:" + sql + "出错，参数：" + boundSql.paramsToString() + e.getMessage(), e);
		} finally {
			closeStatement(ps);
		}
	}

	/**
	 * 
	 * @param sql SELECT * FROM T WHERE name=:name
	 * @param params {name:胡真山}
	 * @return 
	 * @throws SQLException
	 */
	@Override
	public boolean execute(String sql, Map<String, Object> params){
		BoundSql boundSql = new NamedBoundSql(sql, params);
		return this.execute(boundSql);
	}

	/**
	 * 反result 转成   对象
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	public <E> List<E> parseRsToList(ResultSet rs) {
		List<E> dataList = new ArrayList<E>();
		try {
			QueryResult result = new QueryResult(rs);
			while (rs.next()) {
				E bean = this.handler.handle(result);
				dataList.add(bean);
			}
			if (handler.needDoRelate()) {
				this.handler.doRelate(dataList, transaction);
			}
		} catch (SQLException e) {
			throw new SqlRunException("把结果集rs转成List出现错误:" + handler.getResultClass(), e);
		}
		return dataList;
	}
	/**
	 * 从结果集转DataSet
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	public DataSet parseRsToDataSet(ResultSet rs){
		try {
			return ResultUtils.parseToDataSet(rs);
		} catch (SQLException e) {
			throw new SqlRunException("parse to dataset error",e);
		}
	}
	/**
	 * 只读取第一行
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	public <E> E parseRsFirst(ResultSet rs){
		try {
			QueryResult result = new QueryResult(rs);
			E bean=null;
			if (rs.next()) {
				bean = this.handler.handle(result);
			}
			if (handler.needDoRelate()&&bean!=null) {
				List<E> list=CollectionUtils.linkedList(bean);
				this.handler.doRelate(list, transaction);
			}
			return bean;
		} catch (SQLException e) {
			throw new SqlRunException("把结果集rs转成List出现错误:" + handler.getResultClass(), e);
		}
	}

	/**
	 * 反result 转成   对象
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	public <K, E> Map<K, E> parseRsToMap(ResultSet rs, GroupKey<K, E> groupKey){
		Map<K, E> linkedDataMap = new LinkedHashMap<K, E>();
		try {
			QueryResult result = new QueryResult(rs);
			while (rs.next()) {
				E bean = this.handler.handle(result);
				K key = groupKey.getKey(bean);
				linkedDataMap.put(key, bean);
			}
			if (handler.needDoRelate()) {
				this.handler.doRelate(linkedDataMap.values(), transaction);
			}
		} catch (SQLException e) {
			throw new SqlRunException("把结果集rs转成Map出现错误:" + handler.getResultClass(), e);
		}
		return linkedDataMap;
	}

	/**
	 * 反result 转成   对象
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	public <K, E> Map<K, List<E>> parseRsToGroupMap(ResultSet rs, GroupKey<K, E> groupKey){
		Map<K, List<E>> linkedDataMap = new LinkedHashMap<K, List<E>>();
		try {
			QueryResult result = new QueryResult(rs);
			while (rs.next()) {
				E bean = this.handler.handle(result);
				K key = groupKey.getKey(bean);
				List<E> list = linkedDataMap.get(key);
				if (list == null) {
					list = new ArrayList<E>();
					linkedDataMap.put(key, list);
				}
				list.add(bean);
			}
			if (handler.needDoRelate()) {
				this.handler.doRelate(linkedDataMap.values(), transaction);
			}
		} catch (SQLException e) {
			throw new SqlRunException("把结果集rs转成Map出现错误:" + handler.getResultClass(), e);
		}
		return linkedDataMap;
	}

	/**
	 * 支持top 分页的数据库分布查询 
	 * 如果是sql2000以版本 不推荐使用此 方法
	 * @param sql
	 * @param page
	 * @param size
	 * @return
	 * @throws SQLException 
	 */
	@Override
	public <E> PageModel<E> queryTopPageSql(String sql, Object[] params, int page, int size){
		return queryTopPageSql(new ArrayBoundSql(sql, params), page, size);
	}

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
	@Override
	public <E> PageModel<E> queryTopPageSql(BoundSql boundSql, int page, int size){
		int firstRow = (page - 1) * size;
		int lastrow = firstRow + size;
		String sql = boundSql.getSql();
		PreparedStatement ps = null;
		try {
			SQL2000DialectPage dialectPage = new SQL2000DialectPage(sql);
			String countSql = dialectPage.getCountSql();
			logger.debug(countSql);
			ps = transaction.getConnection().prepareStatement(countSql);
			boundSql.fillStatement(ps);
			ResultSet countRs = ps.executeQuery();
			long totals = 0;
			if (countRs.next()) {
				totals = countRs.getLong(1);
			}
			// 关闭记数statement
			ps.close();
			List<E> dataList;
			if (totals == 0l) {
				dataList = new LinkedList<E>();
			} else {
				String dataSql = dialectPage.getTopSql(lastrow);
				logger.debug(dataSql);
				// 数据sql查询
				ps = transaction.getConnection().prepareStatement(dataSql.toString());
				boundSql.fillStatement(ps);
				ResultSet dataRs = ps.executeQuery();
				// 跳过每条记录前面所有的记录
				for (int i = 0; i < firstRow && dataRs.next(); i++)
					;
				// 处理第一条记录以后的数据
				dataList = this.parseRsToList(dataRs);
			}
			return new PageModel<E>(dataList, page, size, totals);
		}catch(Exception e) {
			throw new SqlRunException("query top sql: "+sql,e);
		} finally {
			closeStatement(ps);
		}
	}

	/**
	 * 适用于只能用top的数据库
	 * @param sql
	 * @param params
	 * @param page
	 * @param size
	 * @return
	 * @throws SQLException
	 */
	@Override
	public <E> PageModel<E> queryTopPageSql(String sql, Map params, int page, int size){
		BoundSql boundSql = new NamedBoundSql(sql, params);
		return this.queryTopPageSql(boundSql, page, size);
	}

	/**
	 * 批量执行sql
	 * @param conn
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	@Override
	public int[] batch(String sql, Object[][] params){
		int[] rows = null;
		PreparedStatement stmt = null;
		try {
			logger.debug(sql);
			stmt = transaction.getConnection().prepareStatement(sql);
			ParameterFiller filler = new ArrayParameterFiller();
			for (int i = 0; i < params.length; i++) {
				filler.fillObject(stmt, params[i]);
				stmt.addBatch();
			}
			rows = stmt.executeBatch();
		} catch (Exception e) {
			throw new SqlRunException("批量执行语句出错:" + sql + "出错，参数" + params + e.getMessage(), e);
		} finally {
			closeStatement(stmt);
		}
		return rows;
	}

	/**
	 * 批量操作
	 * @param boundSql
	 * @param list 用于批量操作的赋值列表
	 * @return
	 * @throws SQLException
	 */
	@Override
	public int[] batch(BoundSql boundSql, Collection list){
		int[] rows = null;
		PreparedStatement stmt = null;
		String sql = boundSql.getSql();
		if (logger.isDebugEnabled()) {
			logger.debug("batch sql:" + sql);
			logger.debug("batch paramter: size=" + list.size() + " top 10 " + CollectionUtils.subList(new ArrayList(list), 0, 10));
		}
		try {
			stmt = transaction.getConnection().prepareStatement(sql);
			for (Object obj : list) {
				boundSql.fillBatchStatement(stmt, obj);
				stmt.addBatch();
			}
			rows = stmt.executeBatch();
		} catch (Exception e) {
			throw new SqlRunException("批量执行语句出错:" + sql + "出错，参数 size:" + list.size() + " " + e.getMessage(), e);
		} finally {
			closeStatement(stmt);
		}
		return rows;
	}

	/**
	 * 批量执行sql
	 * @param conn
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	@Override
	public int[] batch(BoundSql boundSql, Object[] params) {
		int[] rows = null;
		PreparedStatement stmt = null;
		String sql = boundSql.getSql();
		if (logger.isDebugEnabled()) {
			logger.debug("batch sql:" + sql);
			logger.debug("batch paramter: size=" + params.length);
		}
		try {
			stmt = transaction.getConnection().prepareStatement(sql);
			for (Object obj : params) {
				boundSql.fillBatchStatement(stmt, obj);
				stmt.addBatch();
			}
			rows = stmt.executeBatch();
		} catch (Exception e) {
			throw new SqlRunException("批量执行语句出错:" + sql + "出错，参数 size:" + params.length + " " + e.getMessage(), e);
		} finally {
			closeStatement(stmt);
		}
		return rows;
	}

	/**
	 * 批量执行sql语句
	 * */
	@Override
	public int[] batch(Collection<String> sqls){
		int[] rows = null;
		try {
			Statement stmt = transaction.getConnection().createStatement();
			for (String sql : sqls) {
				stmt.addBatch(sql);
			}
			rows = stmt.executeBatch();
		} catch (Exception e) {
			throw new SqlRunException("批量执行语句出错，参数" + e.getMessage(), e);
		}
		return rows;
	}

	/**
	 * 设置结果集处理Handler
	 */
	public void setHandler(RowHandler handler) {
		this.handler = handler;
	}

	/**
	 * 执行一条sql语句
	 */
	@Override
	public int executeUpdate(String sql, Map<String, Object> params){
		BoundSql boundSql = new NamedBoundSql(sql, params);
		return this.executeUpdate(boundSql);
	}

	/**
	 * 查询结果为单对象 适用于只有结果只有一行数据的 对象的类型由RowHanndler决定
	 * 可能是一个bean 、list ,array ,map
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	@Override
	public <E> E queryFirst(String sql, Object... params){
		return queryFirst(new ArrayBoundSql(sql, params));
	}

	@Override
	public <E> E queryUninque(String sql, Object... params){
		return queryUninque(new ArrayBoundSql(sql, params));
	}

	@Override
	public <E> E queryForObject(BoundSql boundSql){
		return queryFirst(boundSql);
	}

	/**
	 * 分页查询数据
	 * @param countSql 总条数语句
	 * @param dataSql 数据语句
	 * @param page
	 * @param size
	 * @return
	 */
	@Override
	public <E> PageModel<E> queryPageSql(BoundSql boundSql, int page, int size){
		// 分页数据模型
		PreparedStatement ps = null;
		DialectPage dialectPage = dbDialect.getDialectPage(boundSql.getSql());
		try {
			String countSql = dialectPage.getCountSql();
			logger.debug(countSql);
			ps = transaction.getConnection().prepareStatement(countSql);
			boundSql.fillStatement(ps);
			ResultSet countRs = ps.executeQuery();
			long totals = 0;
			if (countRs.next()) {
				totals = countRs.getLong(1);
				dialectPage.setTotal(totals);
			}
			// 关闭记数statement
			ps.close();
			List<E> dataList;
			if (totals == 0L) {
				dataList = new LinkedList<E>();
			} else {
				String dataSql = dialectPage.getDataSql(page, size);
				ps = transaction.getConnection().prepareStatement(dataSql);
				logger.debug(dataSql);
				boundSql.fillStatement(ps);
				ResultSet dataRs = ps.executeQuery();
				// 处理第一条记录以后的数据
				dataList = this.parseRsToList(dataRs);
			}
			return new PageModel<E>(dataList, page, size, totals);
		} catch (Exception e) {
			throw new SqlRunException("分页查询出错:记数sql:" + dialectPage.getCountSql() + "  数据Sql:" + dialectPage.getDataSql(page, size) + ",参数" + boundSql.paramsToString(), e);
		} finally {
			closeStatement(ps);
		}
	}
	
	@Override
	public <E> List<E> queryLimitSql(BoundSql boundSql, long offset, int limit){
		// 分页数据模型
		PreparedStatement ps = null;
		DialectPage dialectPage = dbDialect.getDialectPage(boundSql.getSql());
		String dataSql=null;
		try {
			dataSql= dialectPage.getLimitSql(offset, limit);
			ps = transaction.getConnection().prepareStatement(dataSql);
			logger.debug(dataSql);
			boundSql.fillStatement(ps);
			ResultSet dataRs = ps.executeQuery();
			// 处理第一条记录以后的数据
			return this.parseRsToList(dataRs);
		} catch (Exception e) {
			throw new SqlRunException("数据Sql:" + dataSql + ",参数" + boundSql.paramsToString(), e);
		} finally {
			closeStatement(ps);
		}
	}

	/**
	 * 查询语句条数
	 * @param boundSql
	 * @return
	 * @throws SQLException
	 */
	public long queryCount(BoundSql boundSql){
		// 分页数据模型
		PreparedStatement ps = null;
		DialectPage dialectPage = dbDialect.getDialectPage(boundSql.getSql());
		try {
			String countSql = dialectPage.getCountSql();
			logger.debug(countSql);
			ps = transaction.getConnection().prepareStatement(countSql);
			boundSql.fillStatement(ps);
			ResultSet countRs = ps.executeQuery();
			long totals = 0;
			if (countRs.next()) {
				totals = countRs.getLong(1);
			}
			return totals;
		}catch(Exception e) {
			throw new SqlRunException(e);
		}finally {
			closeStatement(ps);
		}
	}

	/**
	 * 查询前N条数据
	 * @param sql 数据语句
	 * @param size
	 * @return
	 */
	@Override
	public <E> List<E> queryTop(BoundSql boundSql, int size){

		DialectPage dialectPage = dbDialect.getDialectPage(boundSql.getSql());
		String dataSql = dialectPage.getTopSql(size);
		// 分页数据模型
		PreparedStatement ps = null;
		try {
			logger.debug(dataSql);
			ps = transaction.getConnection().prepareStatement(dataSql);
			boundSql.fillStatement(ps);
			ResultSet dataRs = ps.executeQuery();
			// 处理第一条记录以后的数据
			List<E> dataList = parseRsToList(dataRs);
			return dataList;
		} catch (Exception e) {
			throw new SqlRunException("执行语句出错:" + dataSql + "出错,参数：" + JSONObject.toJSONString(boundSql.getParams()) + e.getMessage(), e);
		} finally {
			closeStatement(ps);
		}
	}

	/**
	 * 分页查询数据
	 * @param countSql
	 * @param dataSql
	 * @param page
	 * @param size
	 * @return
	 */
	@Override
	public <E> PageModel<E> queryPageSql(String sql, int page, int size, Object... params){
		BoundSql boundSql = new ArrayBoundSql(sql, params);
		return this.queryPageSql(boundSql, page, size);
	}

	/**
	 * 分页查询
	 * @param sql select * from t where name=:name
	 * @param params {name:test}
	 * @param page
	 * @param size
	 * @return
	 * @throws SQLException
	 */
	@Override
	public <E> PageModel<E> queryPageSql(String sql, Map params, int page, int size){
		BoundSql boundSql = new NamedBoundSql(sql, params);
		return this.queryPageSql(boundSql, page, size);
	}

	/**
	 * 设置数据库方言
	 * @param dbDialect
	 */
	public void setDbDialect(Dialect dbDialect) {
		this.dbDialect = dbDialect;
	}

	@Override
	public Transaction getTransaction() {
		return transaction;
	}

	/**
	 * 设置事务控制对象
	 * @param transaction
	 */
	@Override
	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	/**
	 * 关闭statement
	 * @param stmt
	 * @throws SQLException
	 */
	protected void closeStatement(Statement stmt){
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				throw new SqlRunException(e);
			}
		}
	}

}
