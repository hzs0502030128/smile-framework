package org.smile.db.sql.page;

import org.smile.commons.Strings;
import org.smile.util.RegExp;

public abstract class AbstractDialectPage implements DialectPage {

	protected final static RegExp FROM_REG = new RegExp(" FROM ", false);
	protected final static RegExp ORDER_BY_REG = new RegExp(" order +by ", false);
	protected final static RegExp TRIM_REG = new RegExp("[ \r\t\n]+");
	protected final static RegExp SELECT_REG = new RegExp("[ \r\t\n]*SELECT[ \r\t\n]+", false);
	/**
	 * 查询语句
	 */
	protected String sql;
	
	public AbstractDialectPage(String sql) {
		this.sql = TRIM_REG.replaceAll(sql.trim(),Strings.SPACE);
	}

	@Override
	public String getCountSql() {
		return getSimpleCountSql();
	}
	
	
	/**
     * 获取普通的Count-sql
     *
     * @param sql 原查询sql
     * @return 返回count查询sql
     */
    protected String getSimpleCountSql(){
    	StringBuilder querySql = new StringBuilder(sql.length() + 50);
		int orderByIndex = ORDER_BY_REG.lastIndex(sql);
		if (orderByIndex <= 0) {
			orderByIndex = sql.length();
		}
		querySql.append("SELECT COUNT(0) FROM (");
		querySql.append(sql.subSequence(0, orderByIndex));
		querySql.append(") COUNT_TABLE_T");
        return querySql.toString();
    }

	@Override
	public String getTopSql(int top) {
		return getDataSql(1, top);
	}

	public String toString() {
		return sql;
	}

	@Override
	public void setTotal(long total) {

	}

	@Override
	public String getSql() {
		return sql;
	}
}
