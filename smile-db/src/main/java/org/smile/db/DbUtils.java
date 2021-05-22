package org.smile.db;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.smile.db.handler.ResultSetMap;
import org.smile.db.sql.ArrayBoundSql;
import org.smile.db.sql.BasicTransaction;
import org.smile.db.sql.BoundSql;
import org.smile.db.sql.SQLRunner;
import org.smile.log.LoggerHandler;
/**
 * 数据库操作工具类
 * @author strive
 *
 */
public class DbUtils implements LoggerHandler{
	
	/**
	 * 查询一条sql语句
	 * @param conn
	 * @param sql
	 * @return
	 * @throws SQLException 
	 * @throws Exception
	 */
	public static List query(Connection conn, String sql,Object... params) throws SQLException {
		List list = null;
		SQLRunner runner = null;
		try {
			runner = new SQLRunner(new BasicTransaction(conn));
			list = runner.query(sql,params);
		}finally {
			runner.getTransaction().close();
		}
		return list;
	}
	
	public static List query(Connection conn,BoundSql boundSql) throws SQLException{
		SQLRunner runner = null;
		try {
			runner = new SQLRunner(new BasicTransaction(conn));
			return runner.query(boundSql);
		}finally {
			runner.getTransaction().close();
		}
	}
	/**
	 * 查询一条sql语句
	 * @param conn
	 * @param sql
	 * @return
	 * @throws SQLException 
	 * @throws Exception
	 */
	public static List queryTop(Connection conn, String sql,int size,Object... params) throws SQLException {
		List list = null;
		SQLRunner runner = null;
		try {
			runner = new SQLRunner(new BasicTransaction(conn));
			list = runner.queryTop(new ArrayBoundSql(sql, params),size);
		}finally {
			runner.getTransaction().close();
		}
		return list;
	}
	/**
	 * 查询一条sql语句前N条记录
	 * @param conn
	 * @param sql
	 * @return
	 * @throws SQLException 
	 * @throws Exception
	 */
	public static List<Map> queryTop(Dialect dialect,Connection conn, String sql,int size,Object... params) throws SQLException {
		List<Map> list = null;
		SQLRunner tool = null;
		try {
			tool = new SQLRunner(new BasicTransaction(conn));
			tool.setDbDialect(dialect);
			list = tool.queryTop(new ArrayBoundSql(sql, params),size);
		}finally {
			tool.getTransaction().close();
		}
		return list;
	}
	/**
	 * 批量执行
	 * @param conn
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public static int[] batch(Connection conn,String sql, Object[][] params) throws SQLException {
		SQLRunner tool = null;
		try {
			tool = new SQLRunner(new BasicTransaction(conn));
			return  tool.batch(sql, params);
		}finally {
			tool.getTransaction().close();
		}
	}
	/**
	 * 执行一条语句
	 * @param conn
	 * @param sql
	 * @throws Exception 
	 * @throws Exception
	 */
	public static boolean execute(Connection conn,String sql,Object... params) throws SQLException {
		SQLRunner tool = null;
		try {
			tool = new SQLRunner(new BasicTransaction(conn));
			return tool.execute(sql,params);
		}finally {
			tool.getTransaction().close();
		}
	}

	/**
	 * 
	 * @param conn
	 * @param sql
	 * @throws Exception
	 */
	public static int executeUpdate(Connection conn,String sql,Object... params) throws SQLException {
		SQLRunner tool = null;
		try {
			tool = new SQLRunner(new BasicTransaction(conn));
			return tool.executeUpdate(sql,params);
		}finally {
			tool.getTransaction().close();
		}
	}
	/**
	 * 查询返回Map 
	 * @param conn 
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public static ResultSetMap queryForMap(Connection conn,String sql,Object... params) throws SQLException{
		SQLRunner tool = null;
		try {
			tool = new SQLRunner(new BasicTransaction(conn));
			return (ResultSetMap) tool.queryFirst(sql,params);
		}finally {
			tool.getTransaction().close();
		}
	}
	/**
	 * 分页查询
	 * @param conn
	 * @param params
	 * @param sql
	 * @param page
	 * @param size
	 * @param dialect
	 * @return
	 * @throws Exception
	 */
	public static PageModel queryPageSql(Connection conn,String sql,int page,int size,Dialect dialect,Object... params) throws SQLException{
		SQLRunner tool = null;
		try {
			tool = new SQLRunner(new BasicTransaction(conn));
			tool.setDbDialect(dialect);
			return tool.queryPageSql(sql, page, size,params);
		}finally {
			tool.getTransaction().close();
		}
	}
	/**
	 * 分页查询
	 * @param conn
	 * @param params
	 * @param sql
	 * @param page
	 * @param size
	 * @param dialect
	 * @return
	 * @throws Exception
	 */
	public static PageModel queryPageSql(Connection conn,String sql,Map params,int page,int size,Dialect dialect) throws SQLException{
		SQLRunner tool = null;
		try {
			tool = new SQLRunner(new BasicTransaction(conn));
			tool.setDbDialect(dialect);
			return tool.queryPageSql(sql,params, page, size);
		}finally {
			tool.getTransaction().close();
		}
	}
	/**
	 * 分页查询
	 * @param conn
	 * @param params
	 * @param sql
	 * @param page
	 * @param size
	 * @param dialect
	 * @return
	 * @throws Exception
	 */
	public static PageModel queryPageSql(Connection conn,BoundSql boundSql,int page,int size,Dialect dialect) throws SQLException{
		SQLRunner tool = null;
		try {
			tool = new SQLRunner(new BasicTransaction(conn));
			tool.setDbDialect(dialect);
			return tool.queryPageSql(boundSql,page, size);
		}finally {
			tool.getTransaction().close();
		}
	}

	/**
	 * 关闭一个连接
	 * @param conn
	 */
	public static void close(Connection conn){
		try {
			if(conn!=null)conn.close();
		} catch (SQLException e) {
			logger.error("关闭数据库连接出现错误",e);
		}
	}
	
	/**
	 * 关闭一个可关闭的对象
	 * @param closeable
	 */
	public static void close(Closeable closeable){
		try {
			if(closeable!=null)closeable.close();
		} catch (SQLException e) {
			logger.error("关闭出现错误",e);
		}
	}
	/**
	 * 关闭
	 * @param ps
	 */
	public static void close(PreparedStatement ps){
		try{
			if(ps!=null)ps.close();
		}catch(SQLException e){
			logger.error(e);
		}
	}
	/**
	 * 
	 * @param t
	 */
	public static void close(Transaction t){
		if(t!=null){
			try {
				t.close();
			} catch (SQLException e) {
				logger.error(e);
			}
		}
	}
	
	public static void rollback(Transaction t){
		if(t!=null){
			try {
				t.rollback();
			} catch (SQLException e) {
				logger.error(t);
			}
		}
	}
}
