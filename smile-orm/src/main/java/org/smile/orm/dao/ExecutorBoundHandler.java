package org.smile.orm.dao;

import java.sql.SQLException;

import org.smile.db.sql.BoundSql;
import org.smile.orm.executor.MappedOperator;
/**
 * 创建boundsql的处理者类
 * @author 胡真山
 *
 */
public interface ExecutorBoundHandler {
	/**
	 * 创建buildsql
	 * @param operator 操作方法
	 * @param param
	 * @return
	 * @throws SQLException 
	 */
	public BoundSql buildBoundSql(MappedOperator operator, Object param) throws SQLException;
	
	public Executor getExecutor();
	
	public void setExecutor(Executor exe);
}
