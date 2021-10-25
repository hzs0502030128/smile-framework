package org.smile.orm.base.impl;

import java.sql.Array;
import java.sql.SQLException;
import java.util.*;

import javax.sql.DataSource;

import org.omg.CORBA.Bounds;
import org.smile.Smile;
import org.smile.beans.BeanUtils;
import org.smile.beans.converter.BaseTypeConverter;
import org.smile.beans.converter.BeanException;
import org.smile.beans.converter.ConvertException;
import org.smile.collection.ArrayUtils;
import org.smile.collection.CollectionUtils;
import org.smile.collection.CollectionUtils.GroupKey;
import org.smile.commons.Strings;
import org.smile.db.AbstractTemplate;
import org.smile.db.PageModel;
import org.smile.db.SqlRunException;
import org.smile.db.Transaction;
import org.smile.db.handler.LikeBeanRowHandler;
import org.smile.db.handler.MapRowHandler;
import org.smile.db.handler.OneFieldRowHandler;
import org.smile.db.handler.ResultSetMap;
import org.smile.db.handler.RowHandler;
import org.smile.db.sql.ArrayBoundSql;
import org.smile.db.sql.BoundSql;
import org.smile.db.sql.NamedBoundSql;
import org.smile.db.sql.SQLRunner;
import org.smile.db.sql.parameter.ParameterFiller;
import org.smile.json.JSON;
import org.smile.orm.ann.TenantId;
import org.smile.orm.base.EnableSupportDAO;
import org.smile.orm.mapping.OrmMapping;
import org.smile.orm.mapping.OrmObjMapping;
import org.smile.orm.mapping.OrmTableMapping;
import org.smile.orm.mapping.property.EnableFlagProperty;
import org.smile.orm.mapping.property.OrmProperty;
import org.smile.orm.tenantId.TenantIdOrmProperty;
import org.smile.util.StringUtils;

public class BaseDAOImpl extends AbstractTemplate implements EnableSupportDAO{
	/**
	 * 属性填充实现
	 */
	protected static ParameterFiller filler = new FieldPropertyFiller();
	
	/**使用对象属性支持的where语句解析*/
	protected WhereSqlBoundBuilder sqlBoundBuilder=new OrmWhereSqlBoundBuilder();
	/**默认的map返回处理*/
	protected MapRowHandler defaultMapRowHandler=MapRowHandler.RESULT_SET_MAP;
	/**
	 * 空构造方法
	 */
	public BaseDAOImpl() {}
	/**
	 * 一个数据源参数的构造方法
	 * @param ds
	 */
	public BaseDAOImpl(DataSource ds) {
		this.dataSource = ds;
	}


	@Override
	public <E> List<E> queryAll(Class<E> c) {
		return this.query(c, null);
	}
	/**
	 * 添加where查询条件
	 * 会自动 添加上where 
	 * @param sql 
	 * @param whereSql 查询条件  不带where关键字
	 * @return
	 * @throws SQLException 
	 */
	protected BoundSql createBoundSql(Class clazz,StringBuilder sql,String whereSql,Object[] params,Object[] newParams){
		return sqlBoundBuilder.build(clazz,sql, whereSql, params,newParams);
	}

	@Override
	public <E> List<E> query(Class<E> c, String whereSql, Object... params) {
		BoundSql boundSql=createBoundSql(c, whereSql, params);
		Transaction transaction = initTransaction();
		try {
			SQLRunner runner = new SQLRunner(transaction, new OrmTableRowHandler(c));
			return runner.query(boundSql);
		} finally {
			endTransaction(transaction);
		}
	}
	@Override
	public <K,E> Map<K,E> queryForKeyMap(Class<E> c,String whereSql,GroupKey<K, E> groupKey,Object ...params){
		BoundSql boundSql=createBoundSql(c, whereSql, params);
		Transaction transaction = initTransaction();
		try {
			SQLRunner runner = new SQLRunner(transaction, new OrmTableRowHandler(c));
			return runner.queryForKeyMap(boundSql, groupKey);
		} finally {
			endTransaction(transaction);
		}
	}
	
	@Override
	public <K,E> Map<K,List<E>> queryForGroupMap(Class<E> c,String whereSql,GroupKey<K, E> groupKey,Object ...params){
		BoundSql boundSql=createBoundSql(c, whereSql, params);
		Transaction transaction=initTransaction();
		try{
			SQLRunner runner = new SQLRunner(transaction,new OrmTableRowHandler(c));
			return runner.queryForGroupMap(boundSql, groupKey);
		}finally{
			endTransaction(transaction);
		}
	}
	/**
	 * 创建查询sql语句
	 * @param c
	 * @param whereSql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	@Override
	public  BoundSql createBoundSql(Class c, String whereSql, Object... params){
		final OrmTableMapping pType = OrmTableMapping.getType(c);
		StringBuilder sql = new StringBuilder(pType.getSelectAllSql());
		return createQuerySql(pType, sql, whereSql, params);
	}
	/**
	 * 构建查询语句信息
	 * @param pType
	 * @param sql
	 * @param whereSql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	protected  BoundSql createQuerySql(final OrmTableMapping pType,StringBuilder sql, String whereSql, Object... params){
		//拼接参数
		Object[] newParams=params;
		StringBuilder otherWhere  = new StringBuilder();
		List<Object> otherParams =new LinkedList<>();
		if(pType.supportDisable()){
			EnableFlagProperty enableProperty=pType.getEnableProperty();
			otherWhere.append(enableProperty.getColumnName()+"="+enableProperty.getPropertyExp());
			otherParams.add(enableProperty.getEnable());
		}
		//是否支持租房ID
		if(pType.hasTenantId()){
			OrmProperty tenantId =pType.getTenantId();
			if(otherWhere.length()>0){
				otherWhere.append(" AND ");
			}
			String tenantIdSql = tenantId.getColumnName()+"="+tenantId.getPropertyExp();
			otherWhere.append(tenantIdSql);
			otherParams.add(OrmTableMapping.getTenantIdLoader().loadCurrentTenantId());
		}
		if(otherWhere.length()>0){
			sql.append(" WHERE ").append(otherWhere);
			newParams = new Object[params.length+otherParams.size()];
			int i=0;
			for(Object obj:otherParams){
				newParams[i++]=obj;
			}
			System.arraycopy(params,0,newParams,otherParams.size(),params.length);
			if(StringUtils.notEmpty(whereSql)){
				sql.append(" AND ");
			}
		}else if(StringUtils.notEmpty(whereSql)){
			sql.append(" WHERE ");
		}
		BoundSql boundSql;
		if (StringUtils.notEmpty(whereSql)) {
			boundSql=createBoundSql(pType.getRawClass(),sql, whereSql,params,newParams);
		}else{
			boundSql=new ArrayBoundSql(sql.toString(), newParams);
		}
		return boundSql;
	}

	@Override
	public int update(Class tableMappingClass,String[] propertyNames, String namedWhereSql, Map<String, Object> params) {
		//拼接更新语句
		final OrmTableMapping pType = OrmTableMapping.getType(tableMappingClass);
		if(pType==null){
			throw new NullPointerException("不存在的ORM类映射"+tableMappingClass);
		}
		StringBuilder updateSql =new StringBuilder(100+namedWhereSql.length());
		Object[] updateParams = new Object[propertyNames.length];
		if (ArrayUtils.isEmpty(propertyNames)) {
			throw new SqlRunException("update field must not empty ");
		} else {
			updateSql.append("UPDATE ").append(pType.getName()).append(" SET ");
			for(int i=0;i<propertyNames.length;i++){
				String field= propertyNames[i];
				OrmProperty p=pType.getProperty(field);
				if(p==null){
					throw new SqlRunException(pType.getRawClass()+"不存在映射了的属性"+field);
				}
				if(i!=0){
					updateSql.append(" , ");
				}
				updateParams[i]=params.get(field);
				updateSql.append(p.getColumnName()).append(" = ").append(p.getPropertyExp());
			}
		}
		BoundSql boundSql = this.createQuerySql(pType, new StringBuilder(),namedWhereSql,params);
		//拼接语句
		boundSql = new ArrayBoundSql(updateSql.append(boundSql.getSql()).toString(), ArrayUtils.append(updateParams,(Object[])boundSql.getParams()));
		Transaction transaction = this.initTransaction();
		try {
			SQLRunner runner = new SQLRunner(transaction);
			return runner.executeUpdate(boundSql);
		} finally {
			this.endTransaction(transaction);
		}
	}
	
	/**
	 * 创建查询sql语句
	 * @param c
	 * @param fields 指定查询的字段
	 * @param whereSql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	@Override
	public BoundSql createBoundSql(Class c,String[] fields, String whereSql, Object... params){
		final OrmTableMapping pType = OrmTableMapping.getType(c);
		StringBuilder sql = new StringBuilder(pType.getSelectSql(fields));
		return createQuerySql(pType, sql, whereSql, params);
	}
	
	@Override
	public <E> E queryById(Class<E> c, Object id) {
		List<E> list = queryByIds(c, id);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
	@Override
	public <E> List<E> queryByIds(Class<E> c, Object... id) {
		BoundSql boundSql=createSelectByIdBound(c, id);
		Transaction transaction = initTransaction();
		try {
			SQLRunner runner = new SQLRunner(transaction, new OrmTableRowHandler(c,true));
			List<E> list = runner.query(boundSql);
			return list;
		} finally {
			endTransaction(transaction);
		}
	}
	/**
	 * 以id查询语句
	 * @param c
	 * @param id
	 * @return
	 */
	protected BoundSql createSelectByIdBound(Class c, Object... id) {
		final OrmTableMapping pType = OrmTableMapping.getType(c);
		StringBuilder sql = new StringBuilder(pType.getSelectByIdSql());
		if (id.length > 1) {
			for (int i = 1; i < id.length; i++) {
				sql.append(" OR ").append(pType.getPrimaryKey()).append("=?");
			}
		}
		return new ArrayBoundSql(sql.toString(), id);
	}
	
	@Override
	public int delete(Class c, String whereSql, Object... params) {
		final OrmTableMapping pType = OrmTableMapping.getType(c);
		StringBuilder sql = new StringBuilder(pType.getDeleteAllSql());
		BoundSql boundSql=null;
		Object[] newParams=params;
		if (StringUtils.notEmpty(whereSql)) {
			sql.append(" WHERE ");
			boundSql=createBoundSql(c,sql, whereSql,params,newParams);
		}else {
			boundSql=new ArrayBoundSql(sql.toString(),params);
		}
		Transaction transaction = initTransaction();
		try {
			SQLRunner runner = new SQLRunner(transaction);
			return runner.executeUpdate(boundSql);
		} finally {
			endTransaction(transaction);
		}
	}

	@Override
	public int deleteById(Class c, Object id) {
		final OrmTableMapping pType = OrmTableMapping.getType(c);
		return delete(pType, id);
	}
	
	@Override
	public  int[] deleteByIds(Class c, Object[] ids) {
		final OrmTableMapping pType = OrmTableMapping.getType(c);
		String sql = pType.getDeleteByIdSql();
		BoundSql boundSql=new ArrayBoundSql(sql);
		Transaction transaction = initTransaction();
		try {
			SQLRunner runner = new SQLRunner(transaction);
			return runner.batch(boundSql, ids);
		} finally {
			endTransaction(transaction);
		}
	}

	@Override
	public int deleteAll(Class c) {
		return this.delete(c, null);
	}

	@Override
	public void add(Object bean) {
		final OrmTableMapping<?> pType = OrmTableMapping.getType(bean.getClass());
		BoundSql boundSql=new OrmTableInsertBoundSql(pType,bean);
		boolean auto=pType.isAtuoincrementPrimaryKey();
		Transaction transaction = initTransaction();
		try {
			SQLRunner runner=new SQLRunner(transaction);
			if(auto){
				OrmProperty keyProperty=pType.getPrimaryProperty().getProperty();
				Object keyValue=runner.insertAutoincrement(boundSql);
				keyValue=BaseTypeConverter.getInstance().convert(keyProperty.getFieldType(), keyValue);
				pType.setPrimarKeyValue(bean, keyValue);
			}else{
				runner.execute(boundSql);
			}
		} catch (ConvertException ee) {
			throw new SqlRunException("执行语句出错："+boundSql.getSql()+Smile.LINE_SEPARATOR+ "参数："+JSON.toJSONString(bean),ee);
		}finally {
			endTransaction(transaction);
		}
	}
	@Override
	public <E> PageModel<E> queryPage(Class<E> clazz, int page, int size, String whereSql, Object... params) {
		final OrmTableMapping<?> pType = OrmTableMapping.getType(clazz);
		StringBuilder sql = new StringBuilder(pType.getSelectAllSql());
		Object[] newparams=params;
		if(pType.supportDisable()){
			EnableFlagProperty enableProperty=pType.getEnableProperty();
			sql.append(" WHERE ");
			sql.append(enableProperty.getColumnName()+"="+enableProperty.getPropertyExp());
			newparams=new Object[params.length+1];
			newparams[0]=enableProperty.getEnable();
			System.arraycopy(params, 0, newparams,1, params.length);
			params=newparams;
			if(StringUtils.notEmpty(whereSql)){
				sql.append(" AND ");
			}
		}else if(StringUtils.notEmpty(whereSql)){
			sql.append(" WHERE ");
		}
		BoundSql boundSql=null;
		if (StringUtils.notEmpty(whereSql)) {
			boundSql=createBoundSql(clazz,sql, whereSql,params,newparams);
		}else {
			boundSql=new ArrayBoundSql(sql.toString(),newparams);
		}
		Transaction transaction = initTransaction();
		try {
			SQLRunner runner = new SQLRunner(transaction, new OrmTableRowHandler(clazz));
			runner.setDbDialect(dialect);
			return runner.queryPageSql(boundSql, page, size);
		}finally {
			endTransaction(transaction);
		}
	}

	@Override
	public void add(List objects) {
		if(CollectionUtils.notEmpty(objects)){
			Object e = objects.get(0);
			OrmTableMapping<?> ptType = OrmTableMapping.getType(e.getClass());
			BoundSql boundSql=new OrmTableInsertBoundSql(ptType);
			Transaction transaction = initTransaction();
			try {
				SQLRunner runner = new SQLRunner(transaction);
				runner.batch(boundSql, objects);
			} finally {
				endTransaction(transaction);
			}
		}
	}

	@Override
	public int update(Object updateObj, String[] propertyNames) {
		Class c = updateObj.getClass();
		final OrmTableMapping pType = OrmTableMapping.getType(c);
		if(pType==null){
			throw new NullPointerException("不存在的ORM类映射"+c);
		}
		StringBuilder updateSql ;
		Collection<OrmProperty> propertys;
		if (ArrayUtils.isEmpty(propertyNames)) {
			propertys = pType.columnPropertys();
			updateSql = new StringBuilder(pType.getUpdateAllSql());
		} else {
			updateSql = pType.getUpdateSql(propertyNames);
			propertys=new LinkedList<OrmProperty>();
			for(String field:propertyNames){
				OrmProperty p=pType.getProperty(field);
				if(p==null){
					throw new SqlRunException(pType.getRawClass()+"不存在映射了的属性"+field);
				}
				propertys.add(p);
			}
		}
		updateSql.append(" WHERE ").append(pType.getPrimaryKey()).append(" = ?");
		BoundSql boundSql=new OrmTableUpdateBoundSql(updateSql.toString(),pType,updateObj, propertys);
		Transaction transaction = initTransaction();
		try {
			SQLRunner runner = new SQLRunner(transaction);
			return runner.executeUpdate(boundSql);
		} finally {
			endTransaction(transaction);
		}
	}

	/**
	 * 删除数据
	 * 
	 * @param pType
	 * @param id 主键值
	 * @throws SQLException
	 */
	private int delete(OrmTableMapping pType, Object id) {
		String sql = pType.getDeleteByIdSql();
		Transaction transaction = initTransaction();
		try {
			SQLRunner runner = new SQLRunner(transaction);
			return runner.executeUpdate(sql.toString(), id);
		} finally {
			endTransaction(transaction);
		}
	}

	@Override
	public void insert(Object obj) {
		add(obj);
	}

	@Override
	public void update(Object obj) {
		update(obj, null);
	}

	@Override
	public void delete(Object obj) {
		OrmTableMapping pType = OrmTableMapping.getType(obj.getClass());
		Object primaryKeyValue=pType.getPrimaryKeyValue(obj);
		if(primaryKeyValue==null){
			throw new SqlRunException("can delete when id value is null "+pType.getRawClass());
		}
		delete(pType,primaryKeyValue);
	}

	@Override
	public <E> E get(E obj) {
		Class c = obj.getClass();
		final OrmTableMapping pType = OrmTableMapping.getType(c);
		String sql = pType.getSelectByIdSql();
		Object key=pType.getPrimaryKeyValue(obj);
		if(key==null){
			throw new SqlRunException("can not load properties when id is null ");
		}
		Transaction transaction = initTransaction();
		try {
			SQLRunner runner = new SQLRunner(transaction, new OrmTableRowHandler(c,true));
			E first = runner.queryFirst(sql,key);
			return first;
		} finally {
			endTransaction(transaction);
		}
	}

	@Override
	public void insertBatch(List  objs) {
		add(objs);
	}

	@Override
	public void updateBatch(List  objs) {
		updateBatch(objs, null);
	}

	@Override
	public void deleteBatch(List  objs) {
		if(CollectionUtils.notEmpty(objs)){
			Class c = objs.get(0).getClass();
			OrmTableMapping pType = OrmTableMapping.getType(c);
			String sql = pType.getDeleteByIdSql();
			Object[][] param = new Object[objs.size()][1];
			int index = 0;
			for (Object obj : objs) {
				param[index++][0] = pType.getPrimaryKeyValue(obj);
			}
			Transaction transaction = initTransaction();
			try {
				SQLRunner runner = new SQLRunner(transaction);
				 runner.batch(sql, param);
			} finally {
				endTransaction(transaction);
			}
		}
	}

	@Override
	public int[] deleteByIds(Class  c, Collection  ids) {
		OrmTableMapping pType = OrmTableMapping.getType(c);
		String sql = pType.getDeleteByIdSql();
		BoundSql boundSql=new ArrayBoundSql(sql);
		Transaction transaction = initTransaction();
		try {
			SQLRunner runner = new SQLRunner(transaction);
			return runner.batch(boundSql, ids);
		} finally {
			endTransaction(transaction);
		}
	}

	@Override
	public void disableByIds(Class  clazz,Collection  ids) {
		enableUpdateByIds(clazz, false, ids);
	}
	
	public void enableUpdateByIds(Class  clazz,boolean enable,Collection  ids) {
		OrmTableMapping pType = OrmTableMapping.getType(clazz);
		if(pType.supportDisable()){
			EnableFlagProperty property=pType.getEnableProperty();
			String sql = pType.getEnableByIdSql();
			Object[][] params=new Object[ids.size()][2];
			int index=0;
			for(Object id:ids){
				params[index][0]=enable?property.getEnable():property.getDisable();
				params[index][1]=id;
				index++;
			}
			Transaction transaction = initTransaction();
			try {
				SQLRunner runner = new SQLRunner(transaction);
				runner.batch(sql, params);
			} finally {
				endTransaction(transaction);
			}
		}else{
			throw new SqlRunException("not support disable "+clazz);
		}
	}

	@Override
	public void enableByIds(Class  clazz,Collection  ids) {
		enableUpdateByIds(clazz, true, ids);
	}

	@Override
	public void enableAll(Class  clazz) {
		enableUpdate(clazz, true, null);
	}

	@Override
	public void disableAll(Class  clazz) {
		enableUpdate(clazz, false, null);
	}

	@Override
	public void disable(Class  clazz, String where, Object... params) {
		enableUpdate(clazz, false, where, params);
	}
	/**
	 * 修改有效性
	 * @param clazz
	 * @param enable true 为有效 false 为无效 
	 * @param whereSql
	 * @param params
	 * @throws SQLException
	 */
	public void enableUpdate(Class  clazz,boolean enable,String whereSql,Object... params){
		OrmTableMapping pType = OrmTableMapping.getType(clazz);
		if(pType.supportDisable()){
			EnableFlagProperty property=pType.getEnableProperty();
			String sql = pType.getEnableAllSql();
			Object[] newParams;
			BoundSql boundSql=null;
			if(StringUtils.notEmpty(whereSql)){
				newParams=new Object[params.length+2];
				newParams[0]=enable?property.getEnable():property.getDisable();
				newParams[1]=enable?property.getDisable():property.getEnable();
				System.arraycopy(params, 0, newParams, 2, params.length);
				StringBuilder enableSql=new StringBuilder(sql);
				enableSql.append(" AND ");
				boundSql=createBoundSql(clazz,enableSql, whereSql,params,newParams);
			}else{
				newParams=new Object[2];
				newParams[0]=enable?property.getEnable():property.getDisable();
				newParams[1]=enable?property.getDisable():property.getEnable();
				boundSql=new ArrayBoundSql(sql,newParams);
			}
			Transaction transaction = initTransaction();
			try {
				SQLRunner runner = new SQLRunner(transaction);
				runner.executeUpdate(boundSql);
			} finally {
				endTransaction(transaction);
			}
		}else{
			throw new SqlRunException("not support enable flag "+pType);
		}
	}

	@Override
	public void enable(Class  clazz, String whereSql, Object... params) {
		enableUpdate(clazz, true, whereSql, params);
	}

	@Override
	public void disable(Object obj) {
		enableUpdate(obj, false);
	}
	/**
	 * 修改有效性
	 * @param obj
	 * @param enable true 为有效 false 无效
	 * @throws SQLException
	 */
	public void enableUpdate(Object obj,boolean enable){
		OrmTableMapping pType = OrmTableMapping.getType(obj.getClass());
		if(pType==null){
			throw new SqlRunException("not find a mapping class "+obj.getClass());
		}
		if(pType.supportDisable()){
			EnableFlagProperty property=pType.getEnableProperty();
			String sql = pType.getEnableByIdSql();
			Object[] params=new Object[2];
			params[0]=enable?property.getEnable():property.getDisable();
			params[1]=pType.getPrimaryKeyValue(obj);
			Transaction transaction = initTransaction();
			try {
				SQLRunner runner = new SQLRunner(transaction);
				runner.executeUpdate(sql, params);
			} finally {
				endTransaction(transaction);
			}
		}else{
			throw new SqlRunException("not support enable flag "+pType);
		}
	}

	@Override
	public void enable(Object obj) {
		enableUpdate(obj, true);
	}

	@Override
	public int[] updateBatch(List  objs, String[] fieldName) {
		if(CollectionUtils.notEmpty(objs)){
			Class c = objs.get(0).getClass();
			final OrmTableMapping pType = OrmTableMapping.getType(c);
			StringBuilder updateSql ;
			Collection<OrmProperty> propertys;
			if (ArrayUtils.isEmpty(fieldName)) {
				propertys = pType.columnPropertys();
				updateSql = new StringBuilder(pType.getUpdateAllSql());
			} else {
				updateSql = pType.getUpdateSql(fieldName);
				propertys=new LinkedList<OrmProperty>();
				for(String field:fieldName){
					OrmProperty ofp=pType.getProperty(field);
					if(ofp==null){
						throw new SqlRunException(pType.getRawClass()+"不存在的属性名："+field);
					}
					propertys.add(ofp);
				}
			}
			updateSql.append(" WHERE ").append(pType.getPrimaryKey()).append(" = ?");
			BoundSql boundSql=new OrmTableUpdateBoundSql(updateSql.toString(),pType, propertys);
			Transaction transaction = initTransaction();
			try {
				SQLRunner runner = new SQLRunner(transaction);
				return runner.batch(boundSql, objs);
			} finally {
				endTransaction(transaction);
			}
		}else{
			return null;
		}
	}

	/**
	 * 修改有效性
	 * @param enable true 为有效 false 无效
	 * @throws SQLException
	 */
	public void enableUpdate(List list,boolean enable){
		if(CollectionUtils.notEmpty(list)){
			OrmTableMapping pType = OrmTableMapping.getType(list.get(0).getClass());
			if(pType.supportDisable()){
				EnableFlagProperty property=pType.getEnableProperty();
				String sql = pType.getEnableByIdSql();
				Object[][] params=new Object[list.size()][2];
				int index=0;
				Object enableValue=enable?property.getEnable():property.getDisable();
				for(Object obj:list){
					params[index][0]=enableValue;
					params[index][1]=pType.getPrimaryKeyValue(obj);
					index++;
				}
				Transaction transaction = initTransaction();
				try {
					SQLRunner runner = new SQLRunner(transaction);
					runner.batch(sql,params);
				} finally {
					endTransaction(transaction);
				}
			}else{
				throw new SqlRunException("not support enable flag "+pType);
			}
		}
	}

	@Override
	public void disableBatch(List list) {
		enableUpdate(list, false);
	}

	@Override
	public void enableBatch(List list) {
		enableUpdate(list, true);
	}


	/**
	 * 设计where语句解析
	 * @param sqlBoundBuilder
	 */
	public void setSqlBoundBuilder(WhereSqlBoundBuilder sqlBoundBuilder) {
		this.sqlBoundBuilder = sqlBoundBuilder;
	}
	
	@Override
	public <E> E queryUinque(Class<E> c, String whereSql, Object... params) {
		BoundSql boundSql=createBoundSql(c,whereSql, params);
		Transaction transaction = initTransaction();
		try {
			SQLRunner runner = new SQLRunner(transaction, new OrmTableRowHandler(c,true));
			return runner.queryUnique(boundSql);
		} finally {
			endTransaction(transaction);
		}
	}

	@Override
	public int update(Class clazz,Object updateObj, String... propertyNames) {
		final OrmTableMapping pType = OrmTableMapping.getType(clazz);
		if(pType==null){
			throw new NullPointerException("不存在的ORM类映射"+clazz);
		}
		StringBuilder updateSql ;
		Collection<OrmProperty> propertys;
		if (ArrayUtils.isEmpty(propertyNames)) {
			propertys = pType.columnPropertys();
			updateSql = new StringBuilder(pType.getUpdateAllSql());
		} else {
			updateSql = pType.getUpdateSql(propertyNames);
			propertys=new LinkedList<OrmProperty>();
			for(String field:propertyNames){
				OrmProperty p=pType.getProperty(field);
				if(p==null){
					throw new SqlRunException(pType.getRawClass()+"不存在映射了的属性"+field);
				}
				propertys.add(p);
			}
		}
		updateSql.append(" WHERE ").append(pType.getPrimaryKey()).append(" = ?");
		BoundSql boundSql=new OrmOtherObjUpdateBoundSql(updateSql.toString(),pType,updateObj, propertys);
		Transaction transaction = initTransaction();
		try {
			SQLRunner runner = new SQLRunner(transaction);
			return runner.executeUpdate(boundSql);
		} finally {
			endTransaction(transaction);
		}
	}

	@Override
	public <E> E queryFirst(Class<E> c, String whereSql, Object... params) {
		BoundSql boundSql=createBoundSql(c,whereSql, params);
		Transaction transaction = initTransaction();
		try {
			SQLRunner runner = new SQLRunner(transaction, new OrmTableRowHandler(c,true));
			return runner.queryFirst(boundSql);
		} finally {
			endTransaction(transaction);
		}
	}
	
	@Override
	public <E> List<E> queryTop(Class<E> c,int top, String whereSql, Object... params) {
		BoundSql boundSql=createBoundSql(c,whereSql, params);
		Transaction transaction = initTransaction();
		try {
			SQLRunner runner = new SQLRunner(transaction, new OrmTableRowHandler(c));
			return runner.queryTop(boundSql, top);
		} finally {
			endTransaction(transaction);
		}
	}
	
	@Override
	public <E> List<E> querySql(Class<E> c, String sql, Object... params) {
		Transaction transaction = initTransaction();
		try {
			SQLRunner runner = new SQLRunner(transaction, new OrmTableRowHandler(c));
			List<E> list = runner.query(sql, params);
			return list;
		} finally {
			endTransaction(transaction);
		}
	}
	
	@Override
	public <K,E> Map<K,E> querySqlForKeyMap(Class<E> c,String sql,GroupKey<K, E> groupKey,Object... params){
		Transaction transaction = initTransaction();
		try {
			SQLRunner runner = new SQLRunner(transaction, new OrmTableRowHandler(c));
			return runner.queryForKeyMap(new ArrayBoundSql(sql, params), groupKey);
		} finally {
			endTransaction(transaction);
		}
	}
	
	
	@Override
	public <E> E load(E obj,String ... propertyNames) {
		Class c = obj.getClass();
		final OrmTableMapping pType = OrmTableMapping.getType(c);
		String sql = null;
		if(ArrayUtils.isEmpty(propertyNames)){
			sql=pType.getSelectByIdSql();
		}else{
			StringBuilder selectSql=pType.getSelectSql(propertyNames);
			selectSql.append(" WHERE ").append(pType.getPrimaryKey()).append("=?");
			sql=selectSql.toString();
		}
		Transaction transaction=initTransaction();
		try {
			final E bean=obj;
			SQLRunner runner = new SQLRunner(transaction, new OrmTableRowHandler(c,true){
				@Override
				protected Object newInstance() {
					return bean;
				}
			});
			Object key=pType.getPrimaryKeyValue(obj);
			if(key==null){
				throw new SqlRunException("can not load properties when id is null ");
			}
			return runner.queryUnique(sql,key);
		} finally {
			endTransaction(transaction);
		}
	}
	
	@Override
	public void add(Class c, Object obj) {
		final OrmTableMapping pType = OrmTableMapping.getType(c);
		BoundSql boundSql=new OrmOtherObjInsertBounSql(pType,obj);
		Transaction transaction = initTransaction();
		try {
			boolean auto=pType.isAtuoincrementPrimaryKey();
			SQLRunner runner=new SQLRunner(transaction);
			if(auto){
				OrmProperty keyproperty=pType.getPrimaryProperty().getProperty();
				Object keyValue=runner.insertAutoincrement(boundSql);
				BeanUtils.setValue(obj, keyproperty.getPropertyName(), keyValue);
			}else{
				runner.execute(boundSql);
			}
		} catch (ConvertException ee) {
			throw new SqlRunException("执行语句出错："+boundSql.getSql()+Smile.LINE_SEPARATOR+ "参数："+JSON.toJSONString(obj),ee);
		}catch (BeanException e) {
			throw new SqlRunException("set atuoincrement result to object error",e);
		}finally {
			endTransaction(transaction);
		}
	}
	@Override
	public void add(Class c, Collection list) {
		if(CollectionUtils.notEmpty(list)){
			Transaction transaction = initTransaction();
			try {
				SQLRunner runner = new SQLRunner(transaction);
				OrmTableMapping ptType = OrmTableMapping.getType(c);
				BoundSql boundSql=new OrmOtherObjInsertBounSql(ptType);
				runner.batch(boundSql, list);
			} finally {
				endTransaction(transaction);
			}
		}
	}
	@Override
	public void update(List objList, String[] fieldname) {
		 updateBatch(objList, fieldname);
	}
	
	@Override
	public long queryCount(Class c, String whereSql, Object... params) {
		BoundSql boundSql=createBoundSql(c, whereSql, params);
		Transaction transaction = initTransaction();
		try {
			SQLRunner runner = new SQLRunner(transaction);
			return runner.queryCount(boundSql);
		} finally {
			endTransaction(transaction);
		}
	}

	@Override
	public void saveOrUpdate(Object obj) {
		Class c = obj.getClass();
		final OrmTableMapping pType = OrmTableMapping.getType(c);
		Object key = pType.getPrimaryKeyValue(obj);
		if (key == null) {
			add(obj);
		} else {
			if(pType.getPrimaryProperty().isBasciDefaultValue(key)){//基本类型的默认值 时是插入 例如：0
				add(obj);
			}else{
				String sql = pType.getSelectByIdSql();
				long count = 0;
				Transaction transaction = initTransaction();
				try {
					SQLRunner runner = new SQLRunner(transaction);
					count = runner.queryCount(new ArrayBoundSql(sql, new Object[] { key }));
				} finally {
					endTransaction(transaction);
				}
				if (count > 0) {
					update(obj);
				} else {
					add(obj);
				}
			}
		}
	}
	
	@Override
	public <E> List<E> queryOneFieldList(Class c,String filed, Class<E> resClass, String whereSql, Object... params) {
		BoundSql boundSql=createBoundSql(c,new String[]{filed},whereSql, params);
		return query(boundSql,OneFieldRowHandler.instance(resClass));
	}
	
	@Override
	public <E> E queryOneField(Class c,String filed, Class<E> resClass, String whereSql, Object... params) {
		BoundSql boundSql=createBoundSql(c,new String[]{filed},whereSql, params);
		return queryFirst(boundSql, OneFieldRowHandler.instance(resClass));
	}
	@Override
	public Integer queryForInt(Class c,String filed, String whereSql, Object... params) {
		BoundSql boundSql=createBoundSql(c,new String[]{filed},whereSql, params);
		return queryFirst(boundSql, OneFieldRowHandler.INTEGER);
	}
	@Override
	public Long queryForLong(Class c,String filed, String whereSql, Object... params) {
		BoundSql boundSql=createBoundSql(c,new String[]{filed},whereSql, params);
		return queryFirst(boundSql, OneFieldRowHandler.LONG);
	}
	@Override
	public String queryForString(Class c,String filed, String whereSql, Object... params) {
		BoundSql boundSql=createBoundSql(c,new String[]{filed},whereSql, params);
		return queryFirst(boundSql, OneFieldRowHandler.STRING);
	}
	@Override
	public Double queryForDouble(Class c,String filed, String whereSql, Object... params) {
		BoundSql boundSql=createBoundSql(c,new String[]{filed},whereSql, params);
		return queryFirst(boundSql, OneFieldRowHandler.DOUBLE);
	}
	@Override
	public List<String> queryForStringList(Class c,String filed, String whereSql, Object... params) {
		BoundSql boundSql=createBoundSql(c,new String[]{filed},whereSql, params);
		return query(boundSql,OneFieldRowHandler.STRING);
	}
	@Override
	public <E> List<E> queryForObjectList(Class c,String[] fields, Class<E> res, String whereSql, Object... params) {
		BoundSql boundSql=createBoundSql(c,fields,whereSql, params);
		return query(boundSql,buildObjRowHandler(res));
	}
	@Override
	public <E> E queryForObject(Class c,String[] fields, Class<E> res, String whereSql, Object... params) {
		BoundSql boundSql=createBoundSql(c,fields,whereSql, params);
		return queryFirst(boundSql,buildObjRowHandler(res));
	}
	@Override
	public List<ResultSetMap> queryForMapList(Class c, String[] fields, String whereSql, Object... params) {
		BoundSql boundSql=createBoundSql(c,fields,whereSql, params);
		return query(boundSql,this.defaultMapRowHandler);
	}
	@Override
	public ResultSetMap queryForMap(Class c, String[] fields, String whereSql, Object... params) {
		BoundSql boundSql=createBoundSql(c,fields,whereSql, params);
		return queryFirst(boundSql,this.defaultMapRowHandler);
	}
	
	@Override
	public <E> void queryMixTo(Class c, Object id,OrmMapping<E> mixMapping, E mixTarget) {
		BoundSql boundSql=createSelectByIdBound(c, id);
		queryMixTo(boundSql, mixMapping, mixTarget);
	}
	
	@Override
	public <E> void queryMixTo(BoundSql boundSql, OrmMapping<E> mixMapping, E mixTarget) {
		Transaction transaction = initTransaction();
		try {
			SQLRunner runner = new SQLRunner(transaction, new MixRowHandler<E>(mixMapping,mixTarget));
			runner.queryUnique(boundSql);
		} finally {
			endTransaction(transaction);
		}
	}
	/**
	 * 构建对象行处理器
	 * @param objClass
	 * @return
	 */
	protected RowHandler buildObjRowHandler(Class objClass) {
		OrmObjMapping mapping=OrmObjMapping.getOrmMapper(objClass);
		if(mapping!=null) {
			return new OrmObjRowHandler(mapping);
		}else {
			return new LikeBeanRowHandler(objClass);
		}
	}
}
