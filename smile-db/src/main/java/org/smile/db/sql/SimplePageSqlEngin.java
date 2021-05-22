package org.smile.db.sql;

import java.sql.SQLException;

import org.smile.commons.SmileRunException;
import org.smile.db.Dialect;
import org.smile.db.sql.page.DialectPage;

public class SimplePageSqlEngin extends PageSqlEngin{

	public SimplePageSqlEngin(Dialect dialect) {
		super(dialect);
	}

	@Override
	protected DialectPage createDialectPage(Dialect dialect, String sql) {
		try {
			return Dialect.newDialectPage(dialect, sql);
		} catch (SQLException e) {
			throw new SmileRunException(e);
		}
	}

}
