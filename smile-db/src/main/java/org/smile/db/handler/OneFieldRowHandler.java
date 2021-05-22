package org.smile.db.handler;

import java.sql.SQLException;
import java.util.Map;

import org.smile.collection.SoftHashMap;
import org.smile.db.result.BasicDatabaseColumn;
import org.smile.db.result.DatabaseColumn;
import org.smile.db.result.QueryResult;
import org.smile.db.result.ResultUtils;

/**
 * 单字段处理封闭
 * @author 胡真山 2015年10月29日
 */
public class OneFieldRowHandler extends RowHandler {
	/*** int 类型单字段*/
	public static final OneFieldRowHandler INTEGER=new OneFieldRowHandler(Integer.class);
	/***string 类型单字段*/
	public static final OneFieldRowHandler STRING=new OneFieldRowHandler(String.class);
	/***long 类型单字段*/
	public static final OneFieldRowHandler LONG=new OneFieldRowHandler(Long.class);
	/***double 类型单字段*/
	public static final OneFieldRowHandler DOUBLE=new OneFieldRowHandler(Double.class);
	
	private static final Map<Class,OneFieldRowHandler> handlers=SoftHashMap.newConcurrentInstance();
	
	protected OneFieldRowHandler(Class resultClass){
		this.resultClass=resultClass;
	}
	
	/**
	 * 实例化一个hanlder
	 * @param resultClass
	 * @return
	 */
	public static OneFieldRowHandler instance(Class resultClass) {
		OneFieldRowHandler handler=handlers.get(resultClass);
		if(handler==null) {
			handler=new OneFieldRowHandler(resultClass);
			handlers.put(resultClass, handler);
		}
		return handler;
	}
	
	@Override
	public <E> E handle(QueryResult rs) throws SQLException {
		DatabaseColumn column=new BasicDatabaseColumn(resultClass,1,rs.getColumnLabel(1));
		return (E)ResultUtils.parseResultColumn(rs.getResultSet(), column);
	}
}
