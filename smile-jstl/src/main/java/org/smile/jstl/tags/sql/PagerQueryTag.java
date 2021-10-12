package org.smile.jstl.tags.sql;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.jsp.JspException;

import org.smile.Smile;
import org.smile.db.DbUtils;
import org.smile.db.Dialect;
import org.smile.db.PageModel;
/**
 * 分页查询标签
 * @author strive
 *
 */
public class PagerQueryTag extends QuerySupportTag {
	/**
	 * 分页模型变量
	 */
	private String var="pageModel";
	/**
	 * 每页显示条数
	 */
	private int size=15;
	/**
	 * 数据起始条，和当前页page只指定一个即可
	 */
	private int offset=0;
	
	/**
	 * 数据库方言
	 */
	private String dialect=Smile.DB_DIALECT;
	/**
	 * 用来保存变量原来的值,以便于标签结束时重新设置回去
	 */
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
			if(size==0){
				size=15;
			}
			Connection conn=getConnection();
			PageModel pageModel=DbUtils.queryPageSql(conn, getBoundSql(),offset/size+1, size,Dialect.of(dialect));
			pageContext.setAttribute(var, pageModel,REQUEST_SCOPE);
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
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public String getDialect() {
		return dialect;
	}
	public void setDialect(String dialect) {
		this.dialect =dialect;
	}
	public int getOffset() {
		return offset;
	}
	public void setOffset(int offset) {
		this.offset = offset;
	}
	
}
