package org.smile.orm.executor.sql;

import java.sql.SQLException;

import org.smile.db.Dialect;
import org.smile.db.sql.BoundSql;
import org.smile.orm.OrmApplication;
import org.smile.orm.executor.BoundSqlBuilder;
import org.smile.orm.executor.MappedOperator;


/**
 * 对sql进行转换的接口
 * @author 胡真山
 *
 */
public interface SqlConverter {
	/**
	 * 对osql进行转换成标准的sql语句
	 * @param osql
	 * @return
	 */
	public String convertToSql(String osql,MappedOperator operator,Dialect dialect);
	
	public void setOrmApplication(OrmApplication application);
	/**
	 * 	单个参数
	 * @param builder
	 * @param sql
	 * @param param
	 * @return
	 * @throws SQLException
	 */
	public BoundSql buildBoundSql(BoundSqlBuilder builder,String sql,Object param) throws SQLException ;
	/**
	 * arraybound
	 * @param builder
	 * @param sql
	 * @param param
	 * @return
	 * @throws SQLException
	 */
	public BoundSql buildBoundSql(BoundSqlBuilder builder,String sql,Object[] param) throws SQLException ;
}
