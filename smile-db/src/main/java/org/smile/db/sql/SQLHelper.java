
package org.smile.db.sql;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.smile.collection.ArrayUtils;
import org.smile.collection.CollectionUtils;
import org.smile.collection.MapUtils;
import org.smile.util.StringUtils;
 /**
  * 
  * @author  胡真山
  *
  */
public class SQLHelper {

	/**
	 *     根据表名和列名称得到插入语句
	 * @param table 要插入的表名
	 * @param names 要插入的列名
	 * @return
	 */
	public static StringBuilder getInsertSql(String table, Collection<String> names) {
		StringBuilder sql = new StringBuilder("INSERT INTO " + table + " (");
	    for(String name:names){
	    	sql.append(name + ",");
	    }
		sql.setLength(sql.length() - 1);
		sql.append(") VALUES (");
		for(int i=0;i<names.size();i++){
			sql.append("?,");
		}
		sql.setLength(sql.length() - 1);
		sql.append(")");
		return sql;
	}
	
	/**
	 * 	根据表名和列名称得到插入语句
	 * @param table 要插入的表名
	 * @param mappings 要插入的列的映射  
	 * @return
	 */
	public static StringBuilder getInsertSql( Collection<? extends Mapping> mappings ,String table) {
		StringBuilder sql = new StringBuilder("INSERT INTO " + table + " (");
	    for(Mapping mapping:mappings){
	    	sql.append(mapping.getColumnName() + ",");
	    }
		sql.setLength(sql.length() - 1);
		sql.append(") VALUES (");
		for(Mapping mapping:mappings){
			sql.append(mapping.getPropertyExp()+",");
		}
		sql.setLength(sql.length() - 1);
		sql.append(")");
		return sql;
	}
	
	/**
	 *      以ID做为删除条件的语句语句
	 * @return
	 */
	public static StringBuilder getDeleteByIdSql(String tableName,String[] keyFileds){
		StringBuilder sql=new StringBuilder(64);
		sql.append("DELETE FROM ");
		sql.append(tableName).append(" WHERE ");
		for(int i=0;i<keyFileds.length;i++){
			sql.append(keyFileds[i]).append("=?");
			if(i<keyFileds.length-1){
				sql.append(" AND ");
			}
		}
		return sql;
	}
	
	/**
	 * 以id做为条件查询语句
	 * @return
	 */
	public static StringBuilder getSelectByIdSql(String tableName,String[] keyFileds){
		StringBuilder sql=new StringBuilder(64);
		sql.append("SELECT * FROM ");
		sql.append(tableName).append(" WHERE ");
		for(int i=0;i<keyFileds.length;i++){
			sql.append(keyFileds[i]).append("=?");
			if(i<keyFileds.length-1){
				sql.append(" AND ");
			}
		}
		return sql;
	}
	/**
	 * 以id查询语句
	 * @param tableName 要查询的表名
	 * @param queryColumns 要查询的所有列名
	 * @param keyFields ID字段名
	 * @return
	 */
	public static String getSelectByIdSql(String tableName,String[] keyFields,String[] queryColumns){
		StringBuilder sql=new StringBuilder(64);
		sql.append("SELECT ");
		for(String s:queryColumns){
			sql.append(s).append(",");
		}
		sql.setLength(sql.length()-1);
		sql.append(" FROM ");
		sql.append(tableName).append(" WHERE ");
		int i=0;
		for(;i<keyFields.length;i++){
			sql.append(keyFields[i]).append("=?");
			if(i<keyFields.length-1){
				sql.append(" AND ");
			}
		}
		return sql.toString();
	}

	
	/**
	 * @param tableName
	 * @param names
	 * @return
	 */
	public static StringBuilder getSelectAllSql(String tableName,  Collection<String>  names) {
		StringBuilder sql = new StringBuilder(64);
		sql.append("SELECT ");
		if( names.size()>0) {
			for (String name : names) {
				sql.append(name + ",");
			}
			sql.setLength(sql.length() - 1);
		}else {
			sql.append("*");
		}
		sql.append(" FROM " + tableName);
		return sql;
	}
	/**
	 *	 根据表名和列名称得到更新语句
	 * 
	 * @param table
	 * @param names
	 * @return
	 */
	public static StringBuilder getUpdateAllSql(String tableName,  Collection<String>  names) {
		StringBuilder sql = new StringBuilder("UPDATE " + tableName + " SET  ");
		for(String name:names){
			sql.append(name + "=?,");
		}
		sql.setLength(sql.length() - 1);
		return sql;
	}
	/**
	 * 根据表名和列名称得到更新语句
	 * 
	 * @param table
	 * @param names
	 * @return
	 */
	public static StringBuilder getUpdateAllSql(String tableName, String[] names) {
		StringBuilder sql = new StringBuilder("UPDATE " + tableName + " SET  ");
		for(String name:names){
			sql.append(name + "=?,");
		}
		sql.setLength(sql.length() - 1);
		return sql;
	}
	
	
	/**
	 * 根据表名和列名称得到更新语句
	 * 
	 * @param table
	 * @param names
	 * @return
	 */
	public static StringBuilder getUpdateAllSql(Collection<? extends Mapping>  mappings,String tableName) {
		StringBuilder sql = new StringBuilder("UPDATE " + tableName + " SET  ");
		for(Mapping mapping:mappings){
			sql.append(mapping.getColumnName() + "="+mapping.getPropertyExp()+",");
		}
		sql.setLength(sql.length() - 1);
		return sql;
	}
 
	/**
	 * 查询所有的语句
	 * @param tableName
	 * @return
	 */
	public static StringBuilder getSelectAllSql(String tableName){
		StringBuilder sql = new StringBuilder(64);
		sql.append("SELECT * FROM ");
		sql.append(tableName);
		return sql;
	}
	
	/**
	 * 查询语句
	 * @param tableName
	 * @param whereSql 条件语句
	 * @return
	 */
	public static StringBuilder getSelectSql(String tableName,String whereSql){
		return getSelectSql(tableName,null,whereSql);
	}
	
	/**
	 * 查询语句
	 * @param tableName 表名
	 * @param queryFields 为空是查询所有字段
	 * @param whereSql 为空时无where条件
	 * @return
	 */
	public static StringBuilder getSelectSql(String tableName,String[] queryFields,String whereSql){
		StringBuilder sql = new StringBuilder(64);
		sql.append("SELECT ");
		if(ArrayUtils.isEmpty(queryFields)){
			sql.append(" * ");
		}else{
			for(int i=0;i<queryFields.length;i++){
				sql.append(queryFields[i]);
				if(i<queryFields.length-1){
					sql.append(",");
				}
			}
		}
		sql.append(" FROM ");
		sql.append(tableName);
		if(StringUtils.notEmpty(whereSql)){
			sql.append(" WHERE ").append(whereSql);
		}
		return sql;
	}
	
	/**
	 * 
	 * @param tableName
	 * @param queryFields
	 * @return
	 */
	public static StringBuilder getSelectSql(String tableName,String[] queryFields){
		return getSelectSql(tableName, queryFields, null);
	}

	/**
	 * 以关键字段构成过滤子句
	 * @return
	 */
	protected static void addKeyFieldWhere(StringBuilder sql, List<Object> params,Map<String,Object> fields,String[] keyFields) {
		sql.append(" WHERE ");
		// 从设置的关键字段构建where字句
		int i = 0, len = keyFields.length;
		for (; i < len; i++) {
			params.add(fields.get(keyFields[i]));
			sql.append(keyFields[i]).append("=").append("?");
			if (i < len - 1) {
				sql.append(" AND ");
			}
			i++;
		}
	}
	/**
	 * 转化成一个UPDATE语句
	 * 
	 * @return UPDATE语句的字符串　
	 */
	public static StringBuilder getUpdateSql(String tableName,String[] updateFields,String whereSql) {
		StringBuilder sql = new StringBuilder(64);
		sql.append("UPDATE ").append(tableName).append(" SET ");
		int i=0,len=updateFields.length;
		for (String key : updateFields) {
			sql.append(key).append("=").append("?");
			if (i < len - 1) {
				sql.append(",");
			}
			i++;
		}
		if (ArrayUtils.notEmpty(whereSql)) {
			sql.append(" WHERE ").append(whereSql);
		}
		return sql;
	}
	
	
	
	
	/**
	 * 删除一个表所有数据的语句
	 * @param tableName
	 * @return
	 */
	public static StringBuilder getDeleteAllSql(String tableName) {
		StringBuilder sql=new StringBuilder(64);
		sql.append("DELETE FROM ").append(tableName);
		return sql;
	}
	
	
	/**
	 * 计数语句
	 * @param tableName 表名
	 * @param where 条件语句
	 * @return
	 */
	public static StringBuilder getCountSql(String tableName,String where){
		StringBuilder sql=new StringBuilder(64);
		sql.append("SELECT COUNT(0) FROM ").append(tableName).append(" WHERE ").append(where);
		return sql;
	}

}
