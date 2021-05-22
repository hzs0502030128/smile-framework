package org.smile.orm.parameter;

import java.sql.SQLException;

import org.smile.db.sql.BoundSql;
import org.smile.orm.executor.SqlBuilder;

/**
 * 方法参数处理者
 * 对方法传入的参数进行分类处理
 * 如果是单属性字段参数  如 integer  long string 等 会转换成 param 参数
 * @author 胡真山
 *
 */
public interface ParameterTypeHandler {
	/***
	 * 处理参数生成boundsql
	 * @param builder
	 * @param args
	 * @return
	 * @throws SQLException 
	 */
	public BoundSql handleBoundSql(SqlBuilder builder,ParameterType parameterType,Object args) throws SQLException;
}
