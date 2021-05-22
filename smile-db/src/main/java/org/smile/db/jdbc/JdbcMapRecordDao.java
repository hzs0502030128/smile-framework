package org.smile.db.jdbc;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.smile.collection.ArrayUtils;
import org.smile.collection.CollectionUtils;
import org.smile.collection.WrapBeanAsMap;
import org.smile.commons.NotImplementedException;
import org.smile.commons.Strings;
import org.smile.db.JdbcTemplate;
import org.smile.db.PageModel;
import org.smile.db.criteria.Criteria;
import org.smile.db.criteria.LambdaCriteria;
import org.smile.db.handler.LikeBeanRowHandler;
import org.smile.db.handler.OneFieldRowHandler;
import org.smile.db.handler.ResultSetMap;
import org.smile.db.handler.RowHandler;
import org.smile.db.sql.BoundSql;
import org.smile.db.sql.SQLHelper;
import org.smile.lambda.Lambda;

public class JdbcMapRecordDao implements EnableRecordDao<JdbcMapRecord> {
	
	protected JdbcTemplate jdbcTemplate;
	
	private TableInfoCfg cfg;
	
	private RecordMapRowHandler rowHandler;
	
	public JdbcMapRecordDao(TableInfoCfg cfg){
		this.cfg=cfg;
		this.jdbcTemplate=JdbcRecordConfig.instance.getJdbcTemplate();
		rowHandler=new RecordMapRowHandler(cfg);
	}

	@Override
	public List<JdbcMapRecord> queryAll() {
		BoundSql boundSql=cfg.getSelectAllBoundSql();
		return jdbcTemplate.query(boundSql, rowHandler);
	}

	@Override
	public List<JdbcMapRecord> query(String whereSql, Object... params) {
		BoundSql boundSql=cfg.getSelectBoundSql(whereSql, params);
		return jdbcTemplate.query(boundSql, rowHandler);
	}

	@Override
	public List<JdbcMapRecord> queryByIds(Object... ids) {
		BoundSql boundSql=cfg.getSelectByIdsSql(ids);
		return jdbcTemplate.query(boundSql, rowHandler);
	}

	@Override
	public JdbcMapRecord queryById(Object id) {
		BoundSql boundSql=cfg.getSelectByIdBoundSql(id);
		return jdbcTemplate.queryFirst(boundSql, rowHandler);
	}
	
	

	@Override
	public int deleteById(Object id) {
		BoundSql boundSql=cfg.getDeleteByIdSql(id);
		return jdbcTemplate.executeUpdate(boundSql);
	}

	@Override
	public int[] deleteByIds(Collection ids) {
		if(CollectionUtils.notEmpty(ids)){
			Object[][] params=new Object[ids.size()][];
			StringBuilder sql=cfg.getDeleteByIdSql();
			int index=0;
			for(Object id:ids){
				params[index++]=cfg.convertIdParam(id);
			}
			return jdbcTemplate.batchUpdate(sql.toString(), params);
		}
		return new int[0];
	}

	@Override
	public int delete(String sqlWhere, Object... params) {
		StringBuilder sql=SQLHelper.getDeleteAllSql(cfg.getTableName());
		sql.append(" WHERE ").append(sqlWhere);
		return jdbcTemplate.executeUpdate(sql.toString(), params);
	}

	@Override
	public int deleteAll() {
		StringBuilder sql=SQLHelper.getDeleteAllSql(cfg.getTableName());
		return jdbcTemplate.executeUpdate(sql.toString());
	}



	@Override
	public void add(JdbcMapRecord obj) {
		if(obj instanceof Map){
			this.jdbcTemplate.insert(this.cfg,(Map)obj);
		}else{
			this.jdbcTemplate.insert(this.cfg,new WrapBeanAsMap(obj));
		}
	}

	@Override
	public void add(List<JdbcMapRecord> c) {
		for(JdbcMapRecord map:c){
			this.jdbcTemplate.insert(map);
		}
	}

	@Override
	public int update(JdbcMapRecord e, String[] fieldName) {
		return jdbcTemplate.update(cfg, e, fieldName);
	}

	@Override
	public int[] updateBatch(List<JdbcMapRecord> list, String[] fieldName) {
		StringBuilder sql=cfg.getBatchUpdateByIdSql(fieldName);
		Object[] params=new Object[list.size()];
		int index=0;
		for(JdbcMapRecord m:list){
			Object[] param=new Object[fieldName.length+cfg.getKeyFields().length];
			int i=0;
			for(String f: fieldName){
				param[i++]=m.get(f);
			}
			for(String f:cfg.getKeyFields()){
				param[i++]=m.get(f);
			}
			params[index++]=param;
		}
		return jdbcTemplate.batchUpdate(sql.toString(), params);
	}

	@Override
	public int[] deleteByIds(Object[] ids) {
		if(ArrayUtils.notEmpty(ids)){
			StringBuilder sql=cfg.getDeleteByIdSql();
			Object[][] params=new Object[ids.length][];
			int index=0;
			for(Object id:ids){
				params[index++]=cfg.convertIdParam(id);
			}
			return jdbcTemplate.batchUpdate(sql.toString(), params);
		}
		return new int[0];
	}

	@Override
	public PageModel<JdbcMapRecord> queryPage(int page, int size, String whereSql, Object[] params) {
		BoundSql boundSql=cfg.getSelectBoundSql(whereSql, params);
		return jdbcTemplate.queryPageSql(rowHandler, boundSql, page, size);
	}

	@Override
	public JdbcMapRecord queryUnique(String whereSql, Object... params) {
		BoundSql boundSql=cfg.getSelectBoundSql(whereSql, params);
		return jdbcTemplate.queryUninque(boundSql, rowHandler);
	}

	@Override
	public JdbcMapRecord queryFirst(String whereSql, Object... params) {
		BoundSql boundSql=cfg.getSelectBoundSql(whereSql, params);
		return jdbcTemplate.queryFirst(boundSql, rowHandler);
	}

	@Override
	public List<JdbcMapRecord> querySql(String sql, Object... params) {
		return jdbcTemplate.query(sql, rowHandler, params);
	}

	@Override
	public void saveOrUpdate(Object obj) {
		Map<String,Object> record;
		if(obj instanceof Map){
			 record=(Map<String,Object>)obj;
		}else{
			record=new WrapBeanAsMap(obj);
		}
		jdbcTemplate.saveOrUpdate(cfg, record);
	}

	@Override
	public void load(Object obj, String... propertyNames) {
		JdbcMapRecord record=(JdbcMapRecord)obj;
		record.load(propertyNames);
	}

	@Override
	public long queryCount(String whereSql, Object... params) {
		BoundSql boundSql=cfg.getCountBoundSql(whereSql, params);
		return jdbcTemplate.queryFirst(boundSql, OneFieldRowHandler.LONG);
	}
	
	@Override
	public Criteria<JdbcMapRecord> criteria() {
		return new JdbcRecordCriteriaImpl(this);
	}

	@Override
	public LambdaCriteria<JdbcMapRecord> lambda() {
		return new JdbcRecordCriteriaImpl(this);
	}

	@Override
	public int update(JdbcMapRecord jdbcMapRecord, Lambda<JdbcMapRecord, ?>[] fieldName) {
		throw new NotImplementedException();
	}

	@Override
	public int update(JdbcMapRecord jdbcMapRecord, Lambda<JdbcMapRecord, ?> first, Lambda<JdbcMapRecord, ?>... others) {
		throw new NotImplementedException();
	}

	@Override
	public int[] updateBatch(List<JdbcMapRecord> list, Lambda<JdbcMapRecord, ?>[] fieldName) {
		throw new NotImplementedException();
	}

	@Override
	public List<JdbcMapRecord> queryTop(int top, String whereSql, Object... params) {
		BoundSql boundSql=cfg.getSelectBoundSql(whereSql, params);
		return jdbcTemplate.queryTop(top,boundSql, rowHandler);
	}

	@Override
	public <T> List<T> queryOneFieldList(String field, Class<T> resClass, String whereSql, Object... params) {
		BoundSql boundSql=cfg.getSelectBoundSql(new String[] {field},whereSql, params);
		return jdbcTemplate.query(boundSql,OneFieldRowHandler.instance(resClass));
	}

	@Override
	public <T> T queryOneField(String field, Class<T> resClass, String whereSql, Object... params) {
		BoundSql boundSql=cfg.getSelectBoundSql(new String[] {field},whereSql, params);
		return jdbcTemplate.queryFirst(boundSql, OneFieldRowHandler.instance(resClass));
	}

	@Override
	public Integer queryForInt(String field, String whereSql, Object... params) {
		BoundSql boundSql=cfg.getSelectBoundSql(new String[] {field},whereSql, params);
		return jdbcTemplate.queryFirst(boundSql,OneFieldRowHandler.INTEGER);
	}

	@Override
	public Long queryForLong(String field, String whereSql, Object... params) {
		BoundSql boundSql=cfg.getSelectBoundSql(new String[] {field},whereSql, params);
		return jdbcTemplate.queryFirst(boundSql,OneFieldRowHandler.LONG);
	}

	@Override
	public String queryForString(String field, String whereSql, Object... params) {
		BoundSql boundSql=cfg.getSelectBoundSql(new String[] {field},whereSql, params);
		return jdbcTemplate.queryFirst(boundSql,OneFieldRowHandler.STRING);
	}

	@Override
	public Double queryForDouble(String field, String whereSql, Object... params) {
		BoundSql boundSql=cfg.getSelectBoundSql(new String[] {field},whereSql, params);
		return jdbcTemplate.queryFirst(boundSql,OneFieldRowHandler.DOUBLE);
	}

	@Override
	public List<String> queryForStringList(String field, String whereSql, Object... params) {
		BoundSql boundSql=cfg.getSelectBoundSql(new String[] {field},whereSql, params);
		return jdbcTemplate.query(boundSql,OneFieldRowHandler.STRING);
	}

	@Override
	public <T> List<T> queryForObjectList(String[] fields, Class<T> res, String whereSql, Object... params) {
		BoundSql boundSql=cfg.getSelectBoundSql(fields,whereSql, params);
		return jdbcTemplate.query(boundSql,new LikeBeanRowHandler(res));
	}

	@Override
	public <T> T queryForObject(String[] fields, Class<T> res, String whereSql, Object... params) {
		BoundSql boundSql=cfg.getSelectBoundSql(fields,whereSql, params);
		return jdbcTemplate.queryFirst(boundSql,new LikeBeanRowHandler(res));
	}

	@Override
	public List<ResultSetMap> queryForMapList(String[] fields, String whereSql, Object... params) {
		BoundSql boundSql=cfg.getSelectBoundSql(fields,whereSql, params);
		return jdbcTemplate.query(boundSql,RowHandler.RESULT_SET_MAP);
	}

	@Override
	public ResultSetMap queryForMap(String[] fields, String whereSql, Object... params) {
		BoundSql boundSql=cfg.getSelectBoundSql(fields,whereSql, params);
		return jdbcTemplate.queryFirst(boundSql,RowHandler.RESULT_SET_MAP);
	}

	@Override
	public List<JdbcMapRecord> queryLimit(long offset, int limit, String whereSql, Object... params) {
		BoundSql boundSql=cfg.getSelectBoundSql(whereSql, params);
		return jdbcTemplate.queryLimit(boundSql,rowHandler,offset,limit);
	}

	@Override
	public <T> List<T> queryLimitObjectList(long offset, int limit, String[] queryFields, Class<T> resClass, String whereSql, Object... params) {
		BoundSql boundSql=cfg.getSelectBoundSql(queryFields,whereSql, params);
		return jdbcTemplate.queryLimit(boundSql,new LikeBeanRowHandler(resClass),offset,limit);
	}

	@Override
	public <T> List<T> queryLimitMapList(long offset, int limit, String[] queryFields, String whereSql, Object... params) {
		BoundSql boundSql=cfg.getSelectBoundSql(queryFields,whereSql, params);
		return jdbcTemplate.queryLimit(boundSql,RowHandler.RESULT_SET_MAP,offset,limit);
	}

	@Override
	public <T> List<T> queryLimitList(long offset, int limit, String[] queryFields, RowHandler rowHandler, String whereSql, Object... params) {
		BoundSql boundSql=cfg.getSelectBoundSql(queryFields,whereSql, params);
		return jdbcTemplate.queryLimit(boundSql,rowHandler,offset,limit);
	}

	@Override
	public void disable(JdbcMapRecord obj) {
		BoundSql boundSql=cfg.getDisabeldByIdBoundSql(obj);
		jdbcTemplate.execute(boundSql);
	}

	@Override
	public void enable(JdbcMapRecord obj) {
		BoundSql boundSql=cfg.getEnabeldByIdBoundSql(obj);
		jdbcTemplate.execute(boundSql);
	}

	@Override
	public void enableAll() {
		StringBuilder sql=SQLHelper.getUpdateAllSql(cfg.getTableName(), new String[] {cfg.enabledField});
		jdbcTemplate.execute(sql.toString(), cfg.enableValue);
	}

	@Override
	public void disableAll() {
		StringBuilder sql=SQLHelper.getUpdateAllSql(cfg.getTableName(), new String[] {cfg.enabledField});
		jdbcTemplate.execute(sql.toString(), cfg.disableValue);
	}

	@Override
	public void disable(String where, Object... params) {
		StringBuilder sql=SQLHelper.getUpdateSql(cfg.getTableName(), new String[] {cfg.enabledField},where);
		jdbcTemplate.execute(sql.toString(), cfg.disableValue);
	}

	@Override
	public void enable(String where, Object... params) {
		StringBuilder sql=SQLHelper.getUpdateSql(cfg.getTableName(), new String[] {cfg.enabledField},where);
		jdbcTemplate.execute(sql.toString(), cfg.enableValue);
	}

	@Override
	public void disableByIds(Collection ids) {
		StringBuilder sql=cfg.getBatchUpdateByIdSql(new String[] {cfg.enabledField});
		Object[][] params=new Object[ids.size()][];
		int idx=0;
		for(Object id:ids) {
			Object[] oneparam;
			if(id instanceof Object[]) {
				Object[] idArray=(Object[])id;
				oneparam=new Object[idArray.length+1];
				oneparam[0]=cfg.disableValue;
				System.arraycopy(idArray, 0, oneparam, 1, idArray.length);
				
			}else {
				oneparam=new Object[] {cfg.disableValue,id};
			}
			params[idx++]=oneparam;
		}
		this.jdbcTemplate.batchUpdate(sql.toString(), params);
	}

	@Override
	public void enableByIds(Collection ids) {
		StringBuilder sql=cfg.getBatchUpdateByIdSql(new String[] {cfg.enabledField});
		Object[][] params=new Object[ids.size()][];
		int idx=0;
		for(Object id:ids) {
			Object[] oneparam;
			if(id instanceof Object[]) {
				Object[] idArray=(Object[])id;
				oneparam=new Object[idArray.length+1];
				oneparam[0]=cfg.enableValue;
				System.arraycopy(idArray, 0, oneparam, 1, idArray.length);
				
			}else {
				oneparam=new Object[] {cfg.enableValue,id};
			}
			params[idx++]=oneparam;
		}
		this.jdbcTemplate.batchUpdate(sql.toString(), params);
	}

	@Override
	public void disableBatch(List<JdbcMapRecord> list) {
		for(JdbcMapRecord record:list) {
			record.disabled();
		}
	}

	@Override
	public void enableBatch(List<JdbcMapRecord> list) {
		for(JdbcMapRecord record:list) {
			record.enabled();
		}
	}

	@Override
	public <T> List<T> query(RowHandler rowHandler, String whereSql, Object... params) {
		BoundSql boundSql=cfg.getSelectBoundSql(whereSql, params);
		return jdbcTemplate.query(boundSql,rowHandler);
	}

	@Override
	public <T> T queryFirst(RowHandler rowHandler, String whereSql, Object... params) {
		BoundSql boundSql=cfg.getSelectBoundSql(whereSql, params);
		return jdbcTemplate.queryFirst(boundSql,rowHandler);
	}

	@Override
	public <T> PageModel<T> queryPage(int page, int size, RowHandler rowHandler, String whereSql, Object[] params) {
		BoundSql boundSql=cfg.getSelectBoundSql(whereSql, params);
		return jdbcTemplate.queryPageSql(rowHandler, boundSql, page, size);
	}

	@Override
	public <T> List<T> queryLimit(long offset, int limit, RowHandler rowHandler, String whereSql, Object... params) {
		BoundSql boundSql=cfg.getSelectBoundSql(whereSql, params);
		return jdbcTemplate.queryLimit(boundSql, rowHandler, offset, limit);
	}

	@Override
	public <T> List<T> queryForObjectList(Class<T> res, String whereSql, Object... params) {
		BoundSql boundSql=cfg.getSelectBoundSql(Strings.EMPTY_ARRAY,whereSql, params);
		return jdbcTemplate.query(boundSql,new LikeBeanRowHandler(res));
	}
	
}
