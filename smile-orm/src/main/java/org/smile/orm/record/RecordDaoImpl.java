package org.smile.orm.record;

import org.smile.commons.Strings;
import org.smile.db.PageModel;
import org.smile.db.criteria.Criteria;
import org.smile.db.criteria.LambdaCriteria;
import org.smile.db.handler.ResultSetMap;
import org.smile.db.handler.RowHandler;
import org.smile.db.jdbc.EnableRecordDao;
import org.smile.db.sql.BoundSql;
import org.smile.lambda.Lambda;
import org.smile.lambda.LambdaUtils;
import org.smile.orm.base.EnableSupportDAO;
import org.smile.orm.base.impl.OrmMapRowHandler;
import org.smile.orm.base.impl.OrmObjRowHandler;
import org.smile.orm.base.impl.OrmTableRowHandler;

import java.util.Collection;
import java.util.List;
/**
 * 数据库记录操作    需指定一个表映射类
 * @author 胡真山
 *
 * @param <E>
 */
public class RecordDaoImpl<E> implements EnableRecordDao<E>{
	/**表对应的映射类*/
	protected Class<E> tableMapClass;
	/**orm操作支持*/
	protected EnableSupportDAO ormDaoSupport=OrmRecordConfig.instance.getOrmDaoSupport();
	/**封装对象的行处理器*/
	protected RowHandler recordRowHandler;
	/**用于封装map的行处理器*/
	protected RowHandler mapRowHandler;
	
	public RecordDaoImpl(Class<E> tableMapClass){
		this.tableMapClass=tableMapClass;
		this.recordRowHandler=new OrmTableRowHandler(tableMapClass);
		this.mapRowHandler=new OrmMapRowHandler(tableMapClass);
	}
	@Override
	public List<E> queryAll() {
		return ormDaoSupport.queryAll(tableMapClass);
	}

	@Override
	public List<E> query(String whereSql, Object... params) {
		return ormDaoSupport.query(tableMapClass, whereSql, params);
	}

	@Override
	public List<E> queryByIds(Object... id) {
		return this.ormDaoSupport.queryByIds(tableMapClass, id);
	}

	@Override
	public E queryById(Object id) {
		return this.ormDaoSupport.queryById(tableMapClass, id);
	}

	@Override
	public int deleteById(Object id) {
		return this.ormDaoSupport.deleteById(tableMapClass, id);
	}

	@Override
	public int[] deleteByIds(Collection ids) {
		return this.ormDaoSupport.deleteByIds(tableMapClass, ids);
	}

	@Override
	public int delete(String sqlWhere, Object... params) {
		return this.ormDaoSupport.delete(tableMapClass, sqlWhere, params);
	}

	@Override
	public int deleteAll() {
		return this.ormDaoSupport.deleteAll(tableMapClass);
	}

	@Override
	public void add(Object obj) {
		Class insertClass=obj.getClass();
		if(insertClass==this.tableMapClass){
			this.ormDaoSupport.add(obj);
		}else{
			this.ormDaoSupport.add(tableMapClass, obj);
		}
	}

	@Override
	public void add(List c) {
		this.ormDaoSupport.add(c);
	}

	@Override
	public int update(E e, String[] fieldName) {
		Class objClass=e.getClass();
		if(objClass==this.tableMapClass){
			return this.ormDaoSupport.update(e, fieldName);
		}else{
			return this.ormDaoSupport.update(tableMapClass,e, fieldName);
		}
	}
	
	@Override
	public int[] updateBatch(List<E> list, String[] fieldName) {
		return this.ormDaoSupport.updateBatch(list, fieldName);
	}

	@Override
	public int[] deleteByIds(Object[] ids) {
		return this.ormDaoSupport.deleteByIds(tableMapClass, ids);
	}

	@Override
	public PageModel<E> queryPage(int page, int size, String whereSql, Object[] params) {
		return this.ormDaoSupport.queryPage(tableMapClass, page, size, whereSql, params);
	}

	@Override
	public E queryUnique(String whereSql, Object... params) {
		return this.ormDaoSupport.queryUinque(tableMapClass, whereSql, params);
	}

	@Override
	public E queryFirst(String whereSql, Object... params) {
		return this.ormDaoSupport.queryFirst(tableMapClass, whereSql, params);
	}

	@Override
	public List<E> querySql(String sql, Object... params) {
		return this.ormDaoSupport.querySql(tableMapClass, sql, params);
	}
	
	public void setOrmDaoSupport(EnableSupportDAO ormDaoSupport) {
		this.ormDaoSupport = ormDaoSupport;
	}
	@Override
	public void disable(E obj) {
		this.ormDaoSupport.disable(obj);
	}
	@Override
	public void enable(E obj) {
		this.ormDaoSupport.enable(obj);
	}
	@Override
	public void enableAll() {
		this.ormDaoSupport.enableAll(tableMapClass);
	}
	@Override
	public void disableAll() {
		this.ormDaoSupport.disableAll(tableMapClass);
	}
	@Override
	public void disable(String where, Object... params) {
		this.ormDaoSupport.disable(tableMapClass, where, params);
	}
	@Override
	public void enable(String where, Object... params) {
		this.ormDaoSupport.enable(tableMapClass, where, params);
	}
	@Override
	public void disableByIds(Collection ids) {
		this.ormDaoSupport.disableByIds(tableMapClass, ids);
	}
	@Override
	public void enableByIds(Collection ids) {
		this.ormDaoSupport.enableByIds(tableMapClass, ids);
	}
	@Override
	public void disableBatch(List<E> list) {
		this.ormDaoSupport.disableBatch(list);
	}
	@Override
	public void enableBatch(List<E> list) {
		this.ormDaoSupport.enableBatch(list);
	}
	@Override
	public void saveOrUpdate(Object obj) {
		this.ormDaoSupport.saveOrUpdate(obj);
	}
	@Override
	public void load(Object obj, String... propertyNames) {
		this.ormDaoSupport.load(obj, propertyNames);
	}
	@Override
	public long queryCount(String whereSql, Object... params) {
		return ormDaoSupport.queryCount(tableMapClass, whereSql, params);
	}
	
	@Override
	public Criteria<E> criteria() {
		return new OrmRecordCriteriaImpl<E>(this);
	}

	@Override
	public LambdaCriteria<E> lambda() {
		return new OrmRecordCriteriaImpl<E>(this);
	}

	@Override
	public int[] updateBatch(List<E> list, Lambda<E,?>[] fieldName) {
		String[] fieldNames=new String[fieldName.length];
		int i=0;
		for(Lambda l:fieldName){
			fieldNames[i]= LambdaUtils.getPropertyName(fieldName[i]);
			i++;
		}
		return this.updateBatch(list,fieldNames);
	}

	@Override
	public int update(E e, Lambda<E,?> first, Lambda<E,?>... others) {
		String[] fieldNames=new String[others.length+1];
		fieldNames[0]=LambdaUtils.getPropertyName(first);
		if(others.length>0) {
			for (int i = 0; i<others.length;i++) {
				fieldNames[i+1] = LambdaUtils.getPropertyName(others[i]);
			}
		}
		return this.update(e,fieldNames);
	}

	@Override
	public int update(E e, Lambda<E,?>[] fieldName) {
		String[] fieldNames=new String[fieldName.length];
		int i=0;
		for(Lambda l:fieldName){
			fieldNames[i]= LambdaUtils.getPropertyName(fieldName[i]);
			i++;
		}
		return this.update(e,fieldNames);
	}

	@Override
	public List<E> queryTop(int top,String whereSql, Object... params) {
		return ormDaoSupport.queryTop(tableMapClass,top, whereSql, params);
	}
	@Override
	public <T> List<T> queryOneFieldList(String field, Class<T> resClass, String whereSql, Object... params) {
		return ormDaoSupport.queryOneFieldList(tableMapClass, field, resClass, whereSql, params);
	}
	@Override
	public <T> T queryOneField(String field, Class<T> resClass, String whereSql, Object... params) {
		return ormDaoSupport.queryOneField(tableMapClass, field, resClass, whereSql, params);
	}
	@Override
	public Integer queryForInt(String field, String whereSql, Object... params) {
		return ormDaoSupport.queryForInt(tableMapClass, field, whereSql, params);
	}
	@Override
	public Long queryForLong(String field, String whereSql, Object... params) {
		return this.ormDaoSupport.queryForLong(tableMapClass, field, whereSql, params);
	}
	@Override
	public String queryForString(String field, String whereSql, Object... params) {
		return this.ormDaoSupport.queryForString(tableMapClass, field, whereSql, params);
	}
	@Override
	public Double queryForDouble(String field, String whereSql, Object... params) {
		return this.ormDaoSupport.queryForDouble(tableMapClass, field, whereSql, params);
	}
	@Override
	public List<String> queryForStringList(String field, String whereSql, Object... params) {
		return this.ormDaoSupport.queryForStringList(tableMapClass, field, whereSql, params);
	}
	@Override
	public <T> List<T> queryForObjectList(String[] fields, Class<T> res, String whereSql, Object... params) {
		return ormDaoSupport.queryForObjectList(tableMapClass, fields, res, whereSql, params);
	}
	
	@Override
	public <T> List<T> queryForObjectList(Class<T> res, String whereSql, Object... params) {
		return ormDaoSupport.queryForObjectList(tableMapClass, Strings.EMPTY_ARRAY, res, whereSql, params);
	}
	
	@Override
	public <T> T queryForObject(String[] fields, Class<T> res, String whereSql, Object... params) {
		return ormDaoSupport.queryForObject(tableMapClass, fields, res, whereSql, params);
	}
	@Override
	public List<ResultSetMap> queryForMapList(String[] fields, String whereSql, Object... params) {
		return ormDaoSupport.queryForMapList(tableMapClass, fields, whereSql, params);
	}
	@Override
	public ResultSetMap queryForMap(String[] fields, String whereSql, Object... params) {
		return ormDaoSupport.queryForMap(tableMapClass, fields, whereSql, params);
	}
	
	@Override
	public List<E> queryLimit(long offset,int limit,String whereSql, Object... params) {
		BoundSql boundSql=ormDaoSupport.createBoundSql(tableMapClass, whereSql, params);
		return this.ormDaoSupport.queryLimit(boundSql,recordRowHandler , offset, limit);
	}
	
	@Override
	public <T> List<T> queryLimitObjectList(long offset, int limit, String[] queryFields, Class<T> resClass, String whereSql, Object... params) {
		BoundSql boundSql=ormDaoSupport.createBoundSql(tableMapClass,queryFields, whereSql, params);
		return this.ormDaoSupport.queryLimit(boundSql, new OrmObjRowHandler(resClass), offset, limit);
	}
	
	@Override
	public <T> List<T> queryLimitMapList(long offset, int limit, String[] queryFields, String whereSql, Object... params) {
		BoundSql boundSql=ormDaoSupport.createBoundSql(tableMapClass,queryFields, whereSql, params);
		return this.ormDaoSupport.queryLimit(boundSql, mapRowHandler, offset, limit);
	}
	
	@Override
	public <T> List<T> queryLimitList(long offset, int limit, String[] queryFields, RowHandler rowHandler, String whereSql, Object... params) {
		BoundSql boundSql=ormDaoSupport.createBoundSql(tableMapClass,queryFields, whereSql, params);
		return this.ormDaoSupport.queryLimit(boundSql, rowHandler, offset, limit);
	}
	
	@Override
	public <T> PageModel<T> queryPage(int page, int size, RowHandler rowHandler, String whereSql, Object[] params) {
		BoundSql boundSql=ormDaoSupport.createBoundSql(tableMapClass, whereSql, params);
		return this.ormDaoSupport.queryPageSql(rowHandler, boundSql, page, size);
	}
	
	@Override
	public <T> List<T> query(RowHandler rowHandler, String whereSql, Object... params) {
		BoundSql boundSql=ormDaoSupport.createBoundSql(tableMapClass, whereSql, params);
		return this.ormDaoSupport.query(boundSql,rowHandler);
	}
	
	@Override
	public <T> T queryFirst(RowHandler rowHandler, String whereSql, Object... params) {
		BoundSql boundSql=ormDaoSupport.createBoundSql(tableMapClass, whereSql, params);
		return this.ormDaoSupport.queryFirst(boundSql,rowHandler);
	}
	
	@Override
	public <T> List<T> queryLimit(long offset, int limit, RowHandler rowHandler, String whereSql, Object... params) {
		BoundSql boundSql=ormDaoSupport.createBoundSql(tableMapClass, whereSql, params);
		return this.ormDaoSupport.queryLimit(boundSql, rowHandler, offset, limit);
	}

}
