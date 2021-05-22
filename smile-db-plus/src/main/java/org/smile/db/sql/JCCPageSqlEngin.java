package org.smile.db.sql;

import java.sql.SQLException;

import org.smile.commons.SmileRunException;
import org.smile.db.Dialect;
import org.smile.db.parser.PageSqlParser;
import org.smile.db.sql.page.DialectPage;
/**
 * 会对sql进行解析后组装分页语句
 * @author 胡真山
 *
 */
public class JCCPageSqlEngin extends PageSqlEngin {

	public JCCPageSqlEngin(Dialect dialect) {
		super(dialect);
	}

	@Override
	protected DialectPage createDialectPage(Dialect dialect, String sql) {
		try {
			return new PageSqlParser(dialect, sql);
		} catch (SQLException e) {
			throw new SmileRunException(e);
		}
	}

}
