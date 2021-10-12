package org.smile.jstl.tags.sql;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.jsp.JspException;

import org.smile.db.DbUtils;
/**
 * 查询标签
 * @author strive
 *
 */
public class QueryTag extends QuerySupportTag {
	/**
	 * 结果变量
	 */
	private String var="list";
	
	private Object oldValue;
	
	@Override
	public int doEndTag() throws JspException {
		if(oldValue==null){
			pageContext.removeAttribute(var,REQUEST_SCOPE);
		}else{
			pageContext.setAttribute(var,oldValue,REQUEST_SCOPE);
		}
		return super.doEndTag();
	}

	@Override
	public int doStartTag() throws JspException {
		oldValue=pageContext.getAttribute(var,REQUEST_SCOPE);
		try {
			List list=DbUtils.query(getConnection(),getBoundSql());
			pageContext.setAttribute(var, list,REQUEST_SCOPE);
		} catch (SQLException e) {
			throw new JspException("sql tag query sql cased a exception :",e);
		}
		return EVAL_BODY_INCLUDE;
	}

	public String getVar() {
		return var;
	}

	public void setVar(String var) {
		this.var = var;
	}

}
