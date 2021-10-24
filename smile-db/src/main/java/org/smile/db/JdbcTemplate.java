package org.smile.db;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.smile.collection.CollectionUtils;
import org.smile.collection.CollectionUtils.GroupKey;
import org.smile.dataset.DataSet;
import org.smile.db.handler.LikeBeanRowHandler;
import org.smile.db.handler.MapRowHandler;
import org.smile.db.handler.OneFieldRowHandler;
import org.smile.db.handler.ResultSetMap;
import org.smile.db.handler.RowHandler;
import org.smile.db.jdbc.JdbcMapRecord;
import org.smile.db.jdbc.JdbcMapper;
import org.smile.db.jdbc.JdbcRecordConfig;
import org.smile.db.jdbc.TableInfoCfg;
import org.smile.db.sql.ArrayBoundSql;
import org.smile.db.sql.BoundSql;
import org.smile.db.sql.NamedBoundSql;
import org.smile.db.sql.SQLHelper;
import org.smile.db.sql.SQLRunner;

/**
 * 一个实现基本功能的数据库访问对象 
 * 对数据库访问进行了一些方便的封装
 * 些方式操作数据库需对事务进行统一管理，在方法中不提供对事务的操作
 * 如使用spring对事务进行管理
 * @author strive 数据库连接是没有处理的 
 */
public class JdbcTemplate extends AbstractTemplate{
	/**
	 * 默认的行处理器
	 */
	protected MapRowHandler mapRowHanlder=RowHandler.RESULT_SET_MAP;

	public JdbcTemplate(){
		//do nothing  used  ioc 
	}

	/**
	 * 
	 * @param ds 指定数据源
	 */
	public JdbcTemplate(DataSource ds) {
		this.dataSource = ds;
	}

	

	/**
	 * 插入一个map中的内容到数据库中
	 * @param map
	 * @throws SQLException
	 */
	public void insert(JdbcMapRecord map){
		TableInfoCfg cfg=map.cfg();
		insert(cfg, map);
	}
	/***
	 * 插入数据
	 * @param cfg
	 * @param map
	 * @throws SQLException
	 */
	public void insert(TableInfoCfg cfg,Map<String,Object> map){
		BoundSql boundSql = cfg.getInsertBoundSql(map);
		Transaction transaction=initTransaction();
		try{
			SQLRunner runner=new SQLRunner(transaction);
			if(cfg.isAutoincrement()){
				Object key=runner.insertAtuoincrement(boundSql);
				map.put(cfg.getKeyFields()[0], key);
			}else{
				runner.executeUpdate(boundSql);
			}
		}finally{
			endTransaction(transaction);
		}
	}
	/**
	 * 删除记录
	 * @param map
	 * @throws SQLException
	 */
	public void delete(JdbcMapRecord map){
		TableInfoCfg cfg=map.cfg();
		BoundSql boundSql =cfg.getDeleteByIdBoundSql(map);
		Transaction transaction=initTransaction();
		try{
			SQLRunner runner=new SQLRunner(transaction);
			runner.execute(boundSql);
		}finally{
			endTransaction(transaction);
		}
	}
	

	/**
	 * 使用主健字段对一个表进行更新操作
	 * @param tableName 表名
	 * @param keyField 主键字段
	 * @param map 所有的字段值key对应字段名称
	 * @throws SQLException
	 */
	public int update(String tableName, String keyField, Map<String, Object> map) {
		TableInfoCfg cfg=new TableInfoCfg(tableName, keyField);
		return update(cfg, map);
	}
	/**
	 * 更新数据
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	public int update(JdbcMapRecord map){
		return update(map.cfg(),map);
	}
	
	/***
	 * 使用id的形式从单表查询一个对象从数据库中 以配置好的map的形式
	 * @param mapper
	 * @param id
	 * @return
	 * @throws SQLException 
	 */
	public <E> E find(JdbcMapper<E> mapper,Object... id){
		BoundSql boundSql=mapper.tableConfig().getSelectByIdBoundSql(id);
		return queryFirst(boundSql, new LikeBeanRowHandler(mapper.mapperClass()));
	}
	
	/**
	 * 更新数据
	 * @param cfg 更新的表配置
	 * @param map 数据map
	 * @return
	 * @throws SQLException
	 */
	public int update(TableInfoCfg cfg,Map<String, Object> map){
		BoundSql boundSql =  cfg.getUpdateBoundSql(map, null);
		Transaction transaction=initTransaction();
		try{
			return new SQLRunner(transaction).executeUpdate(boundSql);
		}finally{
			endTransaction(transaction);
		}
	}
	/**
	 * 保存或修改
	 * @param cfg
	 * @param obj
	 * @return
	 * @throws SQLException
	 */
	public int saveOrUpdate(TableInfoCfg cfg,Map<String, Object> record){
		int i=this.update(cfg,record);
		if(i<=0){
			this.insert(cfg, record);
		}
		return i;
	}
	
	/**
	 * 	更新一个表的指定字段
	 * @param cfg
	 * @param map
	 * @param updateFields
	 * @return
	 * @throws SQLException
	 */
	public int update(TableInfoCfg cfg,Map<String, Object> map,String[] updateFields){
		BoundSql boundSql = cfg.getUpdateBoundSql(map, updateFields);
		Transaction transaction=initTransaction();
		try{
			return new SQLRunner(transaction).executeUpdate(boundSql);
		}finally{
			endTransaction(transaction);
		}
	}

	/**
	 * 查询对象
	 * @param c 返回对象的封装类型
	 * @param whereSql 过滤条件
	 * @return 一个封闭成对象的列表
	 * @throws Exception
	 */
	public <E> List<E> findAll(Class<E> c, String tableName, String whereSql,Object ...params) {
		StringBuilder sql = SQLHelper.getSelectSql(tableName, null, whereSql);;
		Transaction transaction=initTransaction();
		try{
			SQLRunner tool = new SQLRunner(transaction, new LikeBeanRowHandler(c));
			List<E> list = tool.query(new ArrayBoundSql(sql.toString(), params));
			return list;
		}finally{
			endTransaction(transaction);
		}
	}

	/**
	 * 查询对象
	 * 
	 * @param c
	 * @param whereSql 过滤条件
	 * @return
	 * @throws Exception
	 */
	public <E> List<E> findAll(Class<E> c, String tableName) {
		return findAll(c, tableName, null);
	}

	/**
	 * 返回一个Map 封装  
	 * 如数据存在多行也只返回第一行数据 不存在数据时返回null
	 * 
	 * @param sql 查询的语句 可使用 ? 做为占位
	 * @param param 占位符的参数
	 * @return
	 * @throws SQLException
	 */
	public ResultSetMap queryForMap(String sql, Object... param) {
		return queryUninque(new ArrayBoundSql(sql, param), mapRowHanlder);
	}
	/**
	 * 	查询结果以Map类型返回
	 * @param boundSql
	 * @return
	 */
	public ResultSetMap queryForMap(BoundSql boundSql) {
		return queryUninque(boundSql, mapRowHanlder);
	}

	/**
	 * 分页查询对象
	 * 
	 * @param c 返回对象的封装类型
	 * @param whereSql 过滤条件
	 * @return
	 * @throws Exception
	 */
	public <E> PageModel<E> queryPageSql(Class<E> c, String tableName, String whereSql, int page, int size) throws Exception {
		StringBuilder sql = SQLHelper.getSelectSql(tableName, whereSql);
		return queryPageSql(new LikeBeanRowHandler(c), new ArrayBoundSql(sql.toString()), page, size);
	}
	

	/**
	 * 查询对象
	 * 
	 * @param talbeName 表名
	 * @param whereSql 过滤条件
	 * @return
	 * @throws Exception
	 */
	public <E> PageModel<ResultSetMap> queryPageSql(String tableName, String whereSql, int page, int size, String... columns) {
		StringBuilder sql = SQLHelper.getSelectSql(tableName, columns, whereSql);
		return queryPageSql(mapRowHanlder, new ArrayBoundSql(sql.toString()), page, size);
	}

	/**
	 * 查询对象
	 * 
	 * @param c
	 * @param whereSql 过滤条件
	 * @return
	 * @throws Exception
	 */
	public PageModel<ResultSetMap> queryPageSql(String sql, int page, int size, Object... params) {
		return queryPageSql(mapRowHanlder, new ArrayBoundSql(sql, params), page, size);
	}

	/**
	 * 查询语句
	 * 
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public List<ResultSetMap> query(String sql, Object... params) {
		return query(new ArrayBoundSql(sql, params));
	}
	/**
	 * 执行查询 返回map结果集
	 * @param boundSql
	 * @return
	 * @throws SQLException
	 */
	public List<ResultSetMap> query(BoundSql boundSql)  {
		return query(boundSql,mapRowHanlder);
	}
	/**
	 * 以DataSet用为返回结果
	 * @param boundSql
	 * @return
	 * @throws SQLException
	 */
	public DataSet queryDataSet(BoundSql boundSql) {
		Transaction transaction=initTransaction();
		try{
			SQLRunner runner = new SQLRunner(transaction);
			runner.setDbDialect(dialect);
			return runner.queryDataSet(boundSql);
		}finally{
			endTransaction(transaction);
		}
	}
	
	
	
	/**
	 * 查询单字段
	 * @param oneFieldClass 单字段返回类型
	 * @param sql 查询语句 只参是单字段查询
	 * @param params 查询参数
	 * @return 
	 * @throws SQLException
	 */
	public <E> List<E> queryOneFieldList(Class<E> oneFieldClass,String sql,Object ...params){
		Transaction transaction=initTransaction();
		try{
			SQLRunner sqlRuner=new SQLRunner(transaction,OneFieldRowHandler.instance(oneFieldClass));
			return sqlRuner.query(sql,params);
		}finally{
			endTransaction(transaction);
		}
	}
	/**
	 * 查询出一个对象 从数据库的单字段查询
	 * 只返回第一条数据
	 * @param oneFieldClass 单字段返回类型
	 * @param sql 查询语句
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public <E> E queryOneFieldForObject(Class<E> oneFieldClass,String sql,Object ...params){
		return queryFirst(new ArrayBoundSql(sql, params), OneFieldRowHandler.instance(oneFieldClass));
	}
	/**
	 * 返回integer单字段查询
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public List<Integer> queryOneFieldIntList(String sql,Object ...params){
		return query(sql,OneFieldRowHandler.INTEGER,params);
	}
	/**
	 * 返回integer单字段查询 
	 * 只返回第一条数据
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public Integer queryOneFieldForInt(String sql,Object ...params){
		return queryFirst(new ArrayBoundSql(sql, params),OneFieldRowHandler.INTEGER);
	}
	
	/**
	 * 返回integer单字段查询 
	 * 只返回第一条数据
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public Long queryOneFieldForLong(String sql,Object ...params){
		return queryFirst(new ArrayBoundSql(sql, params),OneFieldRowHandler.LONG);
	}
	/**
	 * 返回string单字段查询
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public List<String> queryOneFieldStringList(String sql,Object ...params){
		return query(sql,OneFieldRowHandler.STRING,params);
	}
	/**
	 * 返回string单字段查询
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public String queryOneFieldForString(String sql,Object ...params){
		return queryFirst(new ArrayBoundSql(sql, params),OneFieldRowHandler.STRING);
	}
	/**
	 * 查询语句 以:name 做为占位符的sql语句
	 * 
	 * @param sql 
	 * @param params 可用于替换占位符
	 * @return
	 * @throws SQLException
	 */
	public List<ResultSetMap> queryNamedSql(String sql, Object params) {
		return query(new NamedBoundSql(sql, params));
	}
	
	/**
	 * 查询 一个以 :name占位的语句
	 * @param resultClass 返回结果集的对象封装
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public <E> List<E> queryNamedSql(Class<E> resultClass,String sql,Object params){
		return queryNamedSql(sql, new LikeBeanRowHandler(resultClass), params);
	}
	
	
	/**
	 * 查询一条sql 返回对象的封闭列表
	 * @param sql 语句
	 * @param resultClass 返回的对象封闭类
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public <E> List<E> query(Class<E> resultClass,String sql,Object ... params){
		RowHandler rowHandler=new LikeBeanRowHandler(resultClass);
		BoundSql boundSql=new ArrayBoundSql(sql, params);
		return query(boundSql, rowHandler);
	}
	/**
	 * 以结果对象的一个值做为键 转化为Map
	 * @param sql
	 * @param resultClass
	 * @param groupKey
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public <K,E> Map<K,E> queryForKeyMap(Class<E> resultClass,GroupKey<K, E> groupKey,String sql,Object...params){
		BoundSql boundSql=new ArrayBoundSql(sql, params);
		RowHandler rowHandler=new LikeBeanRowHandler(resultClass);
		return queryForKeyMap(boundSql, rowHandler, groupKey);
	}
	
	/**
	 * 返回分组的Map value-> 封装了ResultSetMap对象的list
	 * @param boundSql
	 * @param groupKey
	 * @return
	 * @throws SQLException
	 */
	public <K> Map<K,List<ResultSetMap>> queryForGroupMap(BoundSql boundSql,GroupKey<K, ResultSetMap> groupKey){
		return queryForGroupMap(boundSql,mapRowHanlder, groupKey);
	}
	
	/**
	 * 把结果值转成Map
	 * @param sql
	 * @param groupKey
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public <K> Map<K,ResultSetMap> queryForKeyMap(GroupKey<K, ResultSetMap> groupKey,String sql,Object...params){
		return queryForKeyMap(new ArrayBoundSql(sql, params), mapRowHanlder,groupKey);
	}
	
	/**
	 * 查询一条语句  
	 * @param sql
	 * @param rowHandler 指定的行转换操作
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public <E> List<E> query(String sql,RowHandler rowHandler,Object ...params){
		Transaction transaction=initTransaction();
		try{
			SQLRunner runner = new SQLRunner(transaction,rowHandler);
			return runner.query(sql,params);
		}finally{
			endTransaction(transaction);
		}
	}
	/**
	 * 以对象方式返回
	 * @param sql 查询的sql语句
	 * @param resultClass 用于封装返回数据的对象
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public <E> E queryForObject(Class<E> resultClass,String sql,Object ... params){
		Transaction transaction=initTransaction();
		try{
			SQLRunner runner = new SQLRunner(transaction,new LikeBeanRowHandler(resultClass));
			return runner.queryUnique(sql, params);
		}finally{
			endTransaction(transaction);
		}
	}
	/**
	 * 返回唯一行
	 * @param sql
	 * @param resultClass
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public <E> E queryUninque(Class<E> resultClass,String sql,Object ... params){
		Transaction transaction=initTransaction();
		try{
			SQLRunner runner = new SQLRunner(transaction,new LikeBeanRowHandler(resultClass));
			return runner.queryUnique(sql, params);
		}finally{
			endTransaction(transaction);
		}
	}
	/**
	 * 返回唯一一条结果
	 * @param boundSql
	 * @param rowHandler
	 * @return
	 * @throws SQLException
	 */
	public <E> E queryUninque(BoundSql boundSql,RowHandler rowHandler){
		Transaction transaction=initTransaction();
		try{
			SQLRunner runner = new SQLRunner(transaction,rowHandler);
			return runner.queryUnique(boundSql);
		}finally{
			endTransaction(transaction);
		}
	}
 
	/**
	 * 执行一条Sql
	 * 
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public boolean execute(String sql, Object... params) {
		return execute(new ArrayBoundSql(sql, params));
	}
	/**
	 * 执行一个语句
	 * @param boundSql
	 * @return
	 * @throws SQLException
	 */
	public boolean execute(BoundSql boundSql){
		Transaction transaction=initTransaction();
		try{
			SQLRunner runner = new SQLRunner(transaction);
			runner.setDbDialect(dialect);
			return runner.execute(boundSql);
		}finally{
			endTransaction(transaction);
		}
	}

	/**
	 * 执行更新
	 * 
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public int executeUpdate(String sql, Object... params) {
		return executeUpdate(new ArrayBoundSql(sql, params));
	}
	/**
	 * 执行update语句
	 * @param boundSql
	 * @return
	 * @throws SQLException
	 */
	public int executeUpdate(BoundSql boundSql) {
		Transaction transaction=initTransaction();
		try{
			SQLRunner runner = new SQLRunner(transaction);
			runner.setDbDialect(dialect);
			return runner.executeUpdate(boundSql);
		}finally{
			endTransaction(transaction);
		}
	}
	
	
	
	/**
	 * 批量处理 更新语句
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public int[] batchUpdate(String sql,Object[] ...params){
		Transaction transaction=initTransaction();
		try{
			SQLRunner runner = new SQLRunner(transaction);
			runner.setDbDialect(dialect);
			return runner.batch(sql, params);
		}finally{
			endTransaction(transaction);
		}
	}
	/**
	 * 批量执行一个以:name行式占位的sql语句  
	 * @param sql
	 * @param params 多个属性可以替换占位符的集合
	 * @return
	 * @throws SQLException
	 */
	public int[] batchNamedSql(String sql,Collection<?> params){
		if(CollectionUtils.notEmpty(params)){
			Transaction transaction=initTransaction();
			try{
				SQLRunner runner = new SQLRunner(transaction);
				runner.setDbDialect(dialect);
				NamedBoundSql boundSql=new NamedBoundSql(sql, CollectionUtils.get(params, 0));
				return runner.batch(boundSql, params);
			}finally{
				endTransaction(transaction);
			}
		}
		return null;
	}
	
	@Override
	public DataSource getDataSource() {
		return dataSource;
	}

	/**
	 * 设置数据源
	 * @param dataSource
	 */
	@Override
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	/***
	 * 设置事务处理控制者
	 * @param transactionHandler
	 */
	public void setTransactionHandler(TransactionHandler transactionHandler) {
		this.transactionHandler = transactionHandler;
	}
	
	/**
	 * 把此模板设置到jdbcrecord配置中
	 */
	public void init2JdbcRecordConfig(){
		JdbcRecordConfig.getInstance().setJdbcTemplate(this);
	}

	public MapRowHandler getMapRowHanlder() {
		return mapRowHanlder;
	}

	/**
	 * 设置map行处理器
	 * @param mapRowHanlder
	 */
	public void setMapRowHanlder(MapRowHandler mapRowHanlder) {
		this.mapRowHanlder = mapRowHanlder;
	}
	
}
