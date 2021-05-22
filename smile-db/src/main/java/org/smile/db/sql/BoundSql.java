package org.smile.db.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;

import org.smile.json.JSON;
import org.smile.log.LoggerHandler;

/**
 * 参数解析
 * @author 胡真山
 */
public abstract class BoundSql implements LoggerHandler {
	/**
	 * 解析之后的sql语句
	 */
	protected String sql;
	
	/**解析后的真正要执行的sql语句*/
	public String getSql(){
		return this.sql;
	}
	
	public void setSql(String sql){
		this.sql=sql;
	}
	/**
	 * 单条件语句执行时赋值
	 * @param ps
	 * @throws SQLException
	 */
	public abstract void fillStatement(PreparedStatement ps) throws SQLException;
	/**
	 * 单语句时的值
	 * @return
	 */
	public abstract Object getParams();
	/**
	 * 批量执行时赋值
	 * @param ps 
	 * @param param 批量当前批量行的要赋的值
	 * @throws SQLException
	 */
	public abstract void fillBatchStatement(PreparedStatement ps,Object param) throws SQLException;
	/**
	 * 迭代出占位的属性映射信息
	 * @return
	 */
	public abstract  Iterator iteratorMapping();
	/**
	 * toString  显示参数
	 * @return
	 */
	public String paramsToString(){
		try{
			return JSON.toJSONString(getParams());
		}catch(Exception e){
			return String.valueOf(getParams());
		}
	}
	
	/**
	 * 用于批量时设置全局的参数
	 * 区另当批量列表之外
	 * 每一次循环都可以取到这个值设置
	 * @param param
	 */
	public void setBatchMap(Map param) {
		///do nothing 
	}
}
