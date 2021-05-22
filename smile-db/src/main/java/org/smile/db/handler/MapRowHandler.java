package org.smile.db.handler;

import java.sql.SQLException;
import java.util.Map;

import org.smile.db.result.QueryResult;
/**
 * 把一行结果集处理成一个Map的实现
 * 
 * @author 胡真山
 *
 */
public class MapRowHandler extends  RowHandler {
	
	protected MapRowHandler() {
		//do nothing 
	}
	
	public MapRowHandler(Class mapClass) {
		this.resultClass=mapClass;
	}
	
	@Override
	public <E> E handle(QueryResult  rs) throws SQLException {
		Map<String, Object> result = buildMap();
		int cols=rs.getColumnCount();
        for (int i = 1; i <= cols; i++) {
        	String columnName = rs.getColumnLabel(i);
            Object value = rs.getObject(i);
            setValue(result, columnName, value);
        }
        return (E)result;
	}
	/**
	 * 创建用于封装行的map
	 * @return
	 * @throws SQLException 
	 */
	protected Map<String,Object> buildMap() throws SQLException{
		if(resultClass.isInterface()) {
			return new ResultSetMap();
		}
		Map<String,Object> resMap;
		try {
			resMap= (Map<String, Object>) resultClass.newInstance();
		} catch (InstantiationException e) {
			resMap=new ResultSetMap();
		} catch (IllegalAccessException e) {
			throw new SQLException("instance class "+resultClass,e);
		}
		return resMap;
	}

	/**
	 * 设置列的内容到map中
	 * @param result
	 * @param columnName
	 * @param value
	 * @throws SQLException
	 */
	protected void setValue(Map<String,Object> result,String columnName,Object value) throws SQLException{
		result.put(columnName, value);
	}

}
