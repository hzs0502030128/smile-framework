package org.smile.db.sql;

import java.sql.SQLException;
import java.util.Map;
import java.util.TreeMap;

import org.smile.db.DbConstans;
import org.smile.db.Dialect;
import org.smile.db.Transaction;
import org.smile.db.handler.RowHandler;
/**
 * 查询
 * @author 胡真山
 *
 */
public class Query {
	/**
	 * 结果集处理
	 */
	private RowHandler handler;
	/**
	 * 查询记录当前页
	 */
	private int page;
	/**
	 * 每页显示条数
	 */
	private int size=10;
	/**
	 * 查询语句
	 */
	private String sql;
	/**
	 * 用来保存参数值
	 */
	private Map paramenters=new TreeMap();
	
	private Dialect dialect;
	
	public Query(String sql){
		this.sql=sql;
		handler=RowHandler.RESULT_SET_MAP;
		dialect=DbConstans.DIALECT;
	}
	public Query(String sql,RowHandler handler){
		this.sql=sql;
		this.handler=handler;
		dialect=DbConstans.DIALECT;
	}
	/**
	 * 设置参数
	 * @param index
	 * @param val
	 */
	@SuppressWarnings("unchecked")
	public void setParameter(int index,Object val){
		paramenters.put(index,val);
	}
	/**
	 * 设置参数
	 * @param name
	 * @param val
	 */
	public void setParameter(String name,Object val){
		paramenters.put(name, val);
	}
	/**
	 * 执行查询
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public Object executeQuery(Transaction conn) throws SQLException{
		BoundSql sqlParams=new NamedBoundSql(sql,paramenters);
		if(page==0){
			return new SQLRunner(conn,handler).query(sqlParams);
		}else{
			return new SQLRunner(conn,handler).queryPageSql(sqlParams,page, size);
		}
	}
	/**
	 * 执行查询
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public Object executeQuery(Transaction conn,Dialect dialect) throws SQLException{
		BoundSql sqlParams=new NamedBoundSql(sql,paramenters);
		if(page==0){
			return new SQLRunner(conn,handler).query(sqlParams);
		}else{
			SQLRunner runner=new SQLRunner(conn,handler);
			runner.setDbDialect(dialect);
			return runner.queryPageSql(sqlParams, page, size);
		}
	}
	/**
	 * 执行查询
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public boolean execute(Transaction conn) throws SQLException{
		BoundSql sqlParams=new NamedBoundSql(sql,paramenters);
		return new SQLRunner(conn).execute(sqlParams.getSql(),sqlParams.getParams());
	}
	
	public void setHandler(RowHandler handler) {
		this.handler = handler;
	}
	
	public void setSql(String sql) {
		this.sql = sql;
	}
	
	public void setPage(int page) {
		this.page = page;
	}
	
	public void setSize(int size) {
		this.size = size;
	}
	
	public void setDialect(Dialect dialect) {
		this.dialect = dialect;
	}
	
}
