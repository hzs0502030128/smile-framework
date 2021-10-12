package org.smile.jstl.tags.sql;

import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;

import org.smile.db.DbUtils;

public class OptionsTag extends QuerySupportTag {
	/**
	 * value字段
	 */
	private String valueField;
	/**
	 * 文字字段
	 */
	private String textField;
	/**
	 * 选中的值
	 */
	private String selected;
	
	public int doStartTag() throws JspException {
		try {
			List list=DbUtils.query(getConnection(), getBoundSql());
			StringBuffer sb=new StringBuffer();
			for(int i=0;i<list.size();i++){
				Map map=(Map)list.get(i);
				sb.append("<option value='").append(map.get(valueField)).append("'");
				if(map.get(valueField).equals(selected)){
					sb.append(" selected ");
				}
				sb.append(">").append(map.get(textField));
				sb.append(" </option>");
			}
			pageContext.getOut().println(sb);
		} catch (Exception e) {
			throw new JspException("sql options tag cased a exception :",e);
		}
		return SKIP_BODY;
	}
	
	public String getValueField() {
		return valueField;
	}
	public void setValueField(String valueField) {
		this.valueField = valueField;
	}
	public String getTextField() {
		return textField;
	}
	public void setTextField(String textField) {
		this.textField = textField;
	}
	public String getSelected() {
		return selected;
	}
	public void setSelected(String selected) {
		this.selected = selected;
	}
}
