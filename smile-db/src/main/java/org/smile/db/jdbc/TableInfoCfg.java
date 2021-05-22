package org.smile.db.jdbc;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.smile.collection.ArrayUtils;
import org.smile.collection.CollectionUtils;
import org.smile.collection.MapUtils;
import org.smile.db.SqlRunException;
import org.smile.db.handler.KeyColumnSwaper;
import org.smile.db.handler.NoneKeyColumnSwaper;
import org.smile.db.sql.ArrayBoundSql;
import org.smile.db.sql.BoundSql;
import org.smile.db.sql.SQLHelper;
import org.smile.util.StringUtils;

/***
 * 表信息设置
 * @author 胡真山
 *
 */
public  class TableInfoCfg{
	
	static String DEFAULT_KEY="id";
	/**表名*/
	protected String tableName;
	/**主键字段*/
	protected String[] keyFields;
	/**是否自动增长*/
	protected boolean autoincrement=false;
	/**
	 * 支持失效软删除的字段
	 */
	protected String enabledField;
	/**生效字段值*/
	protected Object enableValue=true;
	/**失效字段值*/
	protected Object disableValue=false;
	
	protected KeyColumnSwaper keyColumnSwaper=NoneKeyColumnSwaper.instance;
	/**
	 * 构建一个表信息配置
	 * @param tableName 表名
	 * @param keyFields 主键字段
	 */
	public TableInfoCfg(String tableName,String keyFiled){
		this.tableName=tableName;
		this.keyFields=new String[]{keyFiled};
	}
	
	public TableInfoCfg(String tableName){
		this.tableName=tableName;
		this.keyFields=new String[]{DEFAULT_KEY};
	}
	/**
	 * 设置失效字段
	 * @param enabledField
	 * @return
	 */
	public TableInfoCfg enabled(String enabledField) {
		this.enabledField=enabledField;
		return this;
	}
	/**
	 * 是否支持数据失效
	 * @return
	 */
	public boolean enabledSupport() {
		return this.enabledField!=null;
	}
	
	public TableInfoCfg setEnabledValue(Object trueV,Object falseV) {
		this.enableValue=trueV;
		this.disableValue=falseV;
		return this;
	}
	/**
	 * 创建一条空记录
	 * @return
	 */
	public JdbcMapRecord buildRecord(){
		return new JdbcMapRecord(this);
	}
	/**
	 * 表名
	 * @return
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * 主键字段
	 * @return
	 */
	public String[] getKeyFields() {
		return keyFields;
	}

	/**
	 * 是否自动增长
	 * @return
	 */
	public boolean isAutoincrement() {
		return autoincrement;
	}

	/**
	 * 设置成自动增长
	 * @return
	 */
	public TableInfoCfg autoincrement(){
		this.autoincrement=true;
		return this;
	}
	/**
	 * 构建一个表信息配置
	 * @param tableName 表名
	 * @param keyFields 主键字段
	 */
	public TableInfoCfg(String tableName,String[] keyFields){
		this.tableName=tableName;
		this.keyFields=keyFields;
	}
	/**
	 * 表名
	 * @return
	 */
	public String name(){
		return tableName;
	}
	
	/**
	 * 以id做为条件查询语句
	 * @return
	 */
	public BoundSql getSelectByIdBoundSql(Object id){
		StringBuilder sql= SQLHelper.getSelectByIdSql(tableName, keyFields);
		if(enabledSupport()) {
			sql.append(" AND ").append(this.enabledField).append("=?");
			return new ArrayBoundSql(sql.toString(),conertIdParamWithEnable(id));
		}
		return new ArrayBoundSql(sql.toString(),convertIdParam(id));
	}
	
	protected Object[] convertIdParam(Object id){
		Object[] params;
		if(id instanceof Object[]){
			params=(Object[])id;
		}else{
			params=new Object[]{id};
		}
		return params;
	}
	
	protected Object[] conertIdParamWithEnable(Object id) {
		Object[] params;
		if(id instanceof Object[]){
			Object[] ids=(Object[])id;
			params=new Object[ids.length+1];
			System.arraycopy(ids, 0, params, 0, ids.length);
			params[ids.length]=this.enableValue;
		}else{
			params=new Object[]{id,this.enabledField};
		}
		return params;
	}
	
	/**
	 * 获取失效语句
	 * @return
	 */
	public BoundSql getDisabeldBoundSql(String whereSql,Object... params) {
		if(!enabledSupport()) {
			throw new IllegalArgumentException("no enabled field setting");
		}
		StringBuilder sql= SQLHelper.getUpdateAllSql(tableName, new String[] {enabledField});
		sql.append(" WHERE ").append(whereSql);
		Object[] newParams=new Object[params.length+1];
		newParams[0]=this.disableValue;
		System.arraycopy(params, 0, newParams, 1, params.length);
		return new ArrayBoundSql(sql.toString(), newParams);
	}
	
	/**
	 * 获取失效语句
	 * @return
	 */
	public BoundSql getDisabeldByIdBoundSql(Map<String,Object> fieldsMap) {
		if(!enabledSupport()) {
			throw new IllegalArgumentException("no enabled field setting");
		}
		StringBuilder sql= SQLHelper.getUpdateAllSql(tableName, new String[] {enabledField});
		sql.append(" WHERE ");
		List<Object> params=new ArrayList<Object>();
		params.add(this.disableValue);
		initKeyFieldParams(params,fieldsMap, sql);
		return new ArrayBoundSql(sql.toString(), params.toArray());
	}
	
	private void initKeyFieldParams(List<Object> params,Map<String,Object> fieldsMap,StringBuilder sql) {
		// 从设置的关键字段构建where字句
		int i = 0, len = keyFields.length;
		for (; i < len; i++) {
			params.add(fieldsMap.get(keyFields[i]));
			String columnName=keyColumnSwaper.KeyToColumn(keyFields[i]);
			sql.append(columnName).append("=").append("?");
			if (i < len - 1) {
				sql.append(" AND ");
			}
			i++;
		}
	}
	
	public BoundSql getEnabeldByIdBoundSql(Map<String,Object> fieldsMap) {
		if(!enabledSupport()) {
			throw new IllegalArgumentException("no enabled field setting");
		}
		StringBuilder sql= SQLHelper.getUpdateAllSql(tableName, new String[] {enabledField});
		sql.append(" WHERE ");
		List<Object> params=new ArrayList<Object>();
		params.add(this.enableValue);
		initKeyFieldParams(params,fieldsMap, sql);
		return new ArrayBoundSql(sql.toString(),params.toArray());
	}
	
	/**
	 * 删除语句
	 * @return
	 */
	public BoundSql getDeleteByIdSql(Object id){
		StringBuilder sql= SQLHelper.getDeleteByIdSql(tableName, keyFields);
		return new ArrayBoundSql(sql.toString(), convertIdParam(id));
	}
	/**
	 *      以ID为条件删除语句
	 * @param fieldsMap 设置有字段值的Map
	 * @return
	 */
	public BoundSql getDeleteByIdBoundSql(Map<String,Object> fieldsMap) {
		StringBuilder sql= SQLHelper.getDeleteAllSql(tableName);
		List<Object> params=new LinkedList<Object>();
		addKeyFieldWhere(sql, params, fieldsMap);
		return new ArrayBoundSql(sql.toString(),params.toArray());
	}
	
	
	protected StringBuilder getDeleteByIdSql() {
		StringBuilder sql= SQLHelper.getDeleteByIdSql(tableName, keyFields);
		return sql;
	}
	
	
	public BoundSql getCountBoundSql(String whereSql,Object... params) {
		StringBuilder sql=SQLHelper.getCountSql(tableName, whereSql);
		Object[] newParams=params;
		if(enabledSupport()) {
			sql.append(" AND ").append(this.enabledField).append("=?");
			newParams=new Object[params.length+1];
			newParams[0]=this.enableValue;
			System.arraycopy(params, 0, newParams, 1, params.length);
		}
		return new ArrayBoundSql(sql.toString(), newParams);
	}
	/**
	 * 
	 * @param whereSql
	 * @param params
	 * @return
	 */
	public BoundSql getSelectBoundSql(String whereSql, Object... params) {
		return getSelectBoundSql(null, whereSql, params);
	}
	
	/**
	 * 获取查询sql 
	 * @param fields 要查询的字段
	 * @param whereSql 查询的条件语句
	 * @param params 条件参数
	 * @return
	 */
	public BoundSql getSelectBoundSql(String[] fields,String whereSql, Object... params) {
		StringBuilder sql= SQLHelper.getSelectSql(this.tableName, fields);
		Object[] newParams=params;
		if(enabledSupport()) {
			sql.append(" WHERE ").append(enabledField).append("=?");
			if(StringUtils.notEmpty(whereSql)) {
				sql.append(" AND ").append(whereSql);
				newParams=new Object[params.length+1];
				newParams[0]=this.enableValue;
				System.arraycopy(params, 0, newParams, 1, params.length);
			}else {
				newParams=new Object[] {this.enabledField};
			}
		}else if(StringUtils.notEmpty(whereSql)){
			sql.append(" WHERE ").append(whereSql);
		}
		return new ArrayBoundSql(sql.toString(), newParams);
	}
	
	/**
	 * 查询所有的语句
	 * @return
	 */
	public BoundSql getSelectAllBoundSql() {
		StringBuilder sql= SQLHelper.getSelectAllSql(tableName);
		if(enabledSupport()) {
			sql.append(" WHERE ").append(enabledField).append("=?");
			return new ArrayBoundSql(sql.toString(), new Object[] {this.enableValue});
		}
		return new ArrayBoundSql(sql.toString());
	}
	
	/**
	 * 转化成一个SELECT语句
	 * 
	 * 
	 * @return SELECT语句的字符串　
	 */
	public BoundSql getSelectBoundSql(String[] queryFields,Map<String,Object> fieldValues) {
		StringBuilder sql=SQLHelper.getSelectSql(tableName, queryFields);
		List<Object> params=new ArrayList<Object>();
		addKeyFieldWhere(sql, params, fieldValues);
		return new ArrayBoundSql(sql.toString(), params.toArray());
	}
	
	/**
	 * 以关键字段构成过滤子句
	 * @return
	 */
	protected void addKeyFieldWhere(StringBuilder sql, List<Object> params,Map<String,Object> fieldValues) {
		sql.append(" WHERE ");
		if(enabledSupport()) {
			sql.append(this.enabledField).append("=? AND ");
			params.add(this.enableValue);
		}
		// 从设置的关键字段构建where字句
		int i = 0, len = keyFields.length;
		for (; i < len; i++) {
			params.add(fieldValues.get(keyFields[i]));
			String columnName=keyColumnSwaper.KeyToColumn(keyFields[i]);
			sql.append(columnName).append("=").append("?");
			if (i < len - 1) {
				sql.append(" AND ");
			}
			i++;
		}
	}
	/**
	 * 多ID查询语句
	 * @param whereSql
	 * @param ids
	 * @return
	 * @throws SQLException
	 */
	public BoundSql getSelectByIdsSql(Object...ids) {
		StringBuilder sql=SQLHelper.getSelectAllSql(this.tableName);
		String[] keyFields=this.getKeyFields();
		List<Object> params=new ArrayList<Object>();
		if(ids.length>0){
			sql.append(" WHERE ");
			if(enabledSupport()) {
				sql.append(this.enabledField).append("=? AND ");
				params.add(this.enableValue);
			}
			sql.append("(");
			int index=0;
			for(Object id:ids){
				if(index>0){
					sql.append(" OR ");
				}
				if(id instanceof Object[]){
					Object[] keys=(Object[])id;
					if(keyFields.length!=keys.length){
						throw new SqlRunException(" key field ["+StringUtils.join(keyFields,',')+"] args count error");
					}
					sql.append("(");
					String columnName;
					for(int i=0;i<keyFields.length;i++){
						columnName=keyColumnSwaper.KeyToColumn(keyFields[i]);
						sql.append(columnName).append("=?");
						params.add(keys[i]);
						if(i<keyFields.length-1){
							sql.append(" AND ");
						}
					}
					sql.append(")");
				}else if(keyFields.length==1){
					sql.append(keyFields[0]).append("=?");
					params.add(id);
				}else{
					throw new SqlRunException("ids not support args "+ids);
				}
				index++;
			}
			sql.append(")");
		}else{
			throw new SqlRunException("ids must not empty ");
		}
		return new ArrayBoundSql(sql.toString(), params.toArray());
	}

	/**
	 *	 以id更新批定字段
	 * @param updateFields
	 * @param params
	 * @return
	 */
	public BoundSql getUpdateSqlByIdBoundSql(String[] updateFields,Object[] params) {
		StringBuilder sql = new StringBuilder("UPDATE " + tableName + " SET  ");
		for(String name:updateFields){
			sql.append(name + "=?,");
		}
		sql.setLength(sql.length() - 1);
		sql.append(" WHERE ");
		int i=0;
		String columnName;
		for(i=0;i<this.keyFields.length;i++){
			columnName=keyColumnSwaper.KeyToColumn(keyFields[i]);
			sql.append(columnName).append("=?");
			if(i<keyFields.length-1){
				sql.append(" AND ");
			}
		}
		if(enabledSupport()) {
			sql.append(" AND ").append(this.enabledField).append("=?");
			params=ArrayUtils.append(params, this.enableValue);
		}
		return new ArrayBoundSql(sql.toString(), params);
	}
	
	/**
	 * 转化成一个UPDATE语句
	 * 
	 * @return UPDATE语句的字符串　
	 */
	public  BoundSql getUpdateBoundSql(Map<String,Object> fields,String[] updateFields) {
		StringBuilder sql = new StringBuilder(64);
		sql.append("UPDATE ").append(tableName).append(" SET ");
		List<Object> params = new ArrayList<Object>();
		if(ArrayUtils.isEmpty(updateFields)) {
			//需要更新的数据
			Map<String,Object> updateDatas=new HashMap<String,Object>(fields);
			//移除主键不用更新
			MapUtils.removeKeys(updateDatas, keyFields);
			int i = 0, len = updateDatas.size();
			String columnName;
			for (Map.Entry<String, Object> entry: updateDatas.entrySet()) {
				columnName=keyColumnSwaper.KeyToColumn(entry.getKey());
				sql.append(columnName).append("=").append("?");
				params.add(entry.getValue());
				if (i < len - 1) {
					sql.append(",");
				}
				i++;
			}
		}else {
			int i = 0, len = updateFields.length;
			String columnName;
			for (String key : updateFields) {
				columnName=keyColumnSwaper.KeyToColumn(key);
				sql.append(columnName).append("=").append("?");
				params.add(fields.get(key));
				if (i < len - 1) {
					sql.append(",");
				}
				i++;
			}
		}
		if (ArrayUtils.notEmpty(keyFields)) {
			addKeyFieldWhere(sql, params, fields);
		}
		return new ArrayBoundSql(sql.toString(), params.toArray());
	}

	/**
	 * 插入语句
	 * @param fieldsMap
	 * @return
	 */
	public BoundSql getInsertBoundSql(Map<String, Object> fieldsMap) {
		StringBuilder sql = new StringBuilder(64);
		Set<String> keys = new LinkedHashSet<String>(fieldsMap.keySet());
		//去自动增长
		if(autoincrement){
			Set<String> removeKey=CollectionUtils.hashSet(keyFields);
			CollectionUtils.removeAll(keys,removeKey);
		}
		boolean needAddEnable=enabledSupport()&&!fieldsMap.containsKey(enabledField);
		int i = 0, len = keys.size();
		Object[] params = new Object[needAddEnable?len+1:len];
		sql.append("INSERT INTO ").append(tableName).append("(");
		String columnName;
		for (String key : keys) {
			columnName=keyColumnSwaper.KeyToColumn(key);
			sql.append(columnName);
			if (i < len - 1) {
				sql.append(",");
			}
			i++;
		}
		if(needAddEnable) {
			sql.append(",").append(this.enabledField);
		}
		sql.append(") VALUES (");
		i = 0;
		for (String key : keys) {
			sql.append("?");
			params[i] = fieldsMap.get(key);
			if (i < len - 1) {
				sql.append(",");
			}
			i++;
		}
		if(needAddEnable) {
			sql.append(",?");
			params[len]=this.enableValue;
		}
		sql.append(")");
		return new ArrayBoundSql(sql.toString(), params);
	}

	/**
	 * 按id批量更新
	 * @param updateFields
	 * @return
	 */
	public StringBuilder getBatchUpdateByIdSql(String[] updateFields) {
		StringBuilder sql = new StringBuilder("UPDATE " + tableName + " SET  ");
		for(String name:updateFields){
			sql.append(name + "=?,");
		}
		sql.setLength(sql.length() - 1);
		sql.append(" WHERE ");
		int i=0;
		String columnName;
		for(i=0;i<this.keyFields.length;i++){
			columnName=keyColumnSwaper.KeyToColumn(keyFields[i]);
			sql.append(columnName).append("=?");
			if(i<keyFields.length-1){
				sql.append(" AND ");
			}
		}
		return sql;
	}

	public KeyColumnSwaper keyColumnSwaper() {
		return keyColumnSwaper;
	}

	public TableInfoCfg keyColumnSwaper(KeyColumnSwaper keyColumnSwaper) {
		this.keyColumnSwaper = keyColumnSwaper;
		return this;
	}
}
