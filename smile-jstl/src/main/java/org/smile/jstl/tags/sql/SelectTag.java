package org.smile.jstl.tags.sql;

import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;

import org.smile.db.DbUtils;

public class SelectTag extends QuerySupportTag {
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
	/**
	 * cssClass
	 */
	private String css;
	/**
	 * name 属性
	 */
	private String name;
	/**
	 * id 属性
	 */
	private String id;
	/**
	 *  style 属性
	 */
	private String style;

	public int doStartTag() throws JspException {
		try {
			StringBuffer sb=new StringBuffer("<select ").append(id==null?"":" id='"+id+"' ");
			sb.append(name==null?"":"name='"+name+"' ");
			sb.append(style==null?"":"style='"+style+"' ");
			sb.append(css==null?"":"class='"+css+"' ");
			sb.append(">");
			pageContext.getOut().println(sb);
		} catch (Exception e) {
			throw new JspException("sql select tag cased a exception :",e);
		}
		return EVAL_BODY_INCLUDE;
	}
	
	@Override
	public int doEndTag() throws JspException {
		try{
			List list=DbUtils.query(getConnection(),getBoundSql());
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
			sb.append("</select>");
			pageContext.getOut().println(sb);
		}catch(Exception e){
			throw new JspException("sql select tag cased a exception :",e);
		}
		return super.doEndTag();
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

	public String getCss() {
		return css;
	}

	public void setCss(String css) {
		this.css = css;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}
}
