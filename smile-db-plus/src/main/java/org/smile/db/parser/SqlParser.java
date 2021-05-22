package org.smile.db.parser;

import org.smile.db.Dialect;
import org.smile.db.parser.expression.ParserResult;
/**
 * 转换 解析sql语句的接口
 * @author 胡真山
 *
 */
public interface SqlParser {
	/**
	 * 解析转换 sql语句
	 * @param sql
	 * @param dialect
	 * @return
	 */
	public ParserResult parse(String sql,Dialect dialect);
}
