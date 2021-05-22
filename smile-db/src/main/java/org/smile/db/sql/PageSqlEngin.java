package org.smile.db.sql;

import java.util.Map;

import org.smile.collection.SoftHashMap;
import org.smile.db.Dialect;
import org.smile.db.sql.page.DialectPage;
/**
 * 分页引擎
 * @author 胡真山
 *
 */
public abstract class PageSqlEngin implements PageSqlSupport {

	protected Map<String, DialectPage> parserCaches = SoftHashMap.newConcurrentInstance();

	protected Dialect dialect;

	public PageSqlEngin(Dialect dialect) {
		this.dialect = dialect;
	}

	protected abstract DialectPage createDialectPage(Dialect dialect, String sql);

	@Override
	public String getCountSql(String sql) {
		DialectPage dialectPage = getAndCreateDialectPage(sql);
		return dialectPage.getCountSql();
	}
	
	public DialectPage getDialectPage(String sql){
		return getAndCreateDialectPage(sql);
	}

	/***
	 * 
	 * @param sql
	 * @return
	 */
	protected DialectPage getAndCreateDialectPage(String sql) {
		DialectPage dialectPage = parserCaches.get(sql);
		if (dialectPage == null) {
			dialectPage = createDialectPage(dialect, sql);
			parserCaches.put(sql, dialectPage);
		}
		return dialectPage;
	}

	@Override
	public String getDataSql(String sql, int page, int size) {
		DialectPage dialectPage = getAndCreateDialectPage(sql);
		return dialectPage.getDataSql(page, size);
	}

}
