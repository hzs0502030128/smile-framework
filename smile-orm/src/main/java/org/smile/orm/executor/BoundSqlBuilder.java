package org.smile.orm.executor;

import java.sql.SQLException;

import org.smile.db.Dialect;
import org.smile.db.sql.ArrayBoundSql;
import org.smile.db.sql.BoundSql;
import org.smile.log.LoggerHandler;
import org.smile.orm.executor.sql.SqlConverter;

/**
 * 操作语句的创建者
 * @author 胡真山
 */
public class BoundSqlBuilder implements SqlBuilder,LoggerHandler{
	/** * 数据库操作 */
	private MappedOperator operator;
	/**语句转换器*/
	private SqlConverter sqlConverter;
	/**数据库使用的方言*/
	private Dialect dialect;
	/**
	 * 从一个操作方法构造数据库操作
	 * @param operator
	 */
	protected BoundSqlBuilder(MappedOperator operator) {
		this.operator = operator;
		String sqlType = operator.xmlOperator.getSqlType();
		sqlConverter = operator.application.getSqlConverter(sqlType);
		dialect = operator.application.getExecutor().getDialect();
	}
	
	@Override
	public BoundSql buildNullParameterBoundSql(){
		return new ArrayBoundSql(sqlConverter.convertToSql((String)operator.sqlTemplate.getTemplateSource(),operator, dialect));
	}
	/**
	 * 创建sql构造
	 * @param templateParam
	 * @param sqlParam
	 * @return
	 * @throws SQLException
	 */
	@Override
	public BoundSql buildSql(Object templateParam, Object sqlParam) throws SQLException {
		String sql = operator.sqlTemplate.processToString(templateParam);
		logger.debug(sql);
		sql = sqlConverter.convertToSql(sql,operator, dialect);
		return sqlConverter.buildBoundSql(this, sql, sqlParam);
	}

	/**
	 * 创建sql构造
	 * @param templateParam
	 * @param sqlParam
	 * @return
	 * @throws SQLException
	 */
	@Override
	public BoundSql buildSql(Object templateParam, Object[] sqlParam) throws SQLException {
		String sql = operator.sqlTemplate.processToString(templateParam);
		sql = sqlConverter.convertToSql(sql,operator, dialect);
		return sqlConverter.buildBoundSql(this, sql, sqlParam);
	}

}
