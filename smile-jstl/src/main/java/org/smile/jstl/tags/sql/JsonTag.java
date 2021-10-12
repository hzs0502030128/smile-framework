package org.smile.jstl.tags.sql;
import java.sql.Connection;
import java.util.List;

import javax.servlet.jsp.JspException;

import org.smile.db.DbUtils;
import org.smile.json.JSON;

public class JsonTag extends QuerySupportTag {
	
	public int doStartTag() throws JspException {
		Connection conn;
		try {
			List list=DbUtils.query(getConnection(), getBoundSql());
			pageContext.getOut().println(JSON.toJSONString(list));
		} catch (Exception e) {
			throw new JspException("sql options tag cased a exception :",e);
		}
		return SKIP_BODY;
	}
	
}
