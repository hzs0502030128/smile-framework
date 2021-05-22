package org.smile.db.handler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import org.smile.db.Transaction;
import org.smile.db.result.QueryResult;
import org.smile.log.LoggerHandler;
/**
 * 处理一行结果集的接口
 * @author strive
 *
 */
public abstract class RowHandler implements LoggerHandler{
	/**结果集转换成Map的一个实现*/
	public static final MapRowHandler RESULT_SET_MAP=new MapRowHandler(ResultSetMap.class);
	/**对点会处理成一个子map的一个实现*/
	public static final MapRowHandler EXP_RESULT_SET_MAP=new ExpressMapRowHandler(ResultSetMap.class);
	/**驼峰式的map实现*/
	public static final MapRowHandler HUMP_RESULT_SET_MAP=new MapRowHandler(HumpResultSetMap.class);
	/**转换成数组的一个实现*/
	public static final RowHandler ARRAY=new ArrayRowHandler();
	/**转换成list的一个实现*/
	public static final RowHandler ARRAY_LIST=new ListRowHandler(ArrayList.class);
	
	/**要转换成对象的实现类*/
	protected Class resultClass;
	/**
	 * 处理结果集
	 * @param rs 
	 * @return 
	 * @throws Exception
	 */
	public abstract <E> E handle(QueryResult rs) throws SQLException ;
	/**
	 * 处理关联表
	 * @param bean
	 * @param conn
	 * @throws SQLException
	 */
	public <E> void doRelate(Collection<E> bean,Transaction conn) throws SQLException{
		
	}
	/**
	 * 一行数据转换的结果类型
	 * @return
	 */
	public Class getResultClass(){
		return resultClass;
	}
	/***
	 * 是否需要处理对其它对象的关联
	 */
	public boolean needDoRelate(){
		return false;
	}
}
