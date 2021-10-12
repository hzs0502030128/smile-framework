package org.smile.jstl.tags.form;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.smile.commons.StringBand;
import org.smile.util.StringUtils;

public class CheckBoxTag extends TagSupport{
	
	private String id;
	
	private String name;
	
	private String checkedValue="true";
	
	private String uncheckedValue="false";
	
	private Object value;
	
	private String cssClass;
	
	@Override
	public int doStartTag() throws JspException {
		if(StringUtils.isEmpty(id)){
			id=name;
		}
		StringBand sb=new StringBand();
		sb.append("<input type='hidden' name='").append(name).append("'").append("  id='").append(id).append("'");
		sb.append(" value='");
		if(value!=null && checkedValue.equals(String.valueOf(value))){
			sb.append(checkedValue);
		}else{
			sb.append(uncheckedValue);
		}
		sb.append("'/>");
		sb.append("<input onclick='");
		sb.append("var checkbox=document.getElementById(\""+id+"_checkbox\");");
	   	sb.append("var hiddenInput=document.getElementById(\""+id+"\");");
	   	sb.append("if(checkbox.checked){hiddenInput.value=\"").append(checkedValue).append("\"}else{hiddenInput.value=\"").append(uncheckedValue).append("\"}");
		sb.append(";' type='checkbox' id='").append(id).append("_checkbox' name='").append(name).append("_checkbox'");
		if(value!=null && checkedValue.equals(String.valueOf(value))){
			sb.append(" checked");
		}
		if(StringUtils.notEmpty(cssClass)){
			sb.append(" class='").append(cssClass).append("'");
		}
		sb.append("/>");
		try {
			pageContext.getOut().print(sb.toString());
		} catch (IOException e) {
			throw new JspException(e);
		}
		return super.doStartTag();
	}
	public String getCheckedValue() {
		return checkedValue;
	}
	public void setCheckedValue(String checkedValue) {
		this.checkedValue = checkedValue;
	}
	public String getUncheckedValue() {
		return uncheckedValue;
	}
	public void setUncheckedValue(String uncheckedValue) {
		this.uncheckedValue = uncheckedValue;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCssClass() {
		return cssClass;
	}
	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}
}
