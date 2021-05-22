package org.smile.strate.jslt.tag;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.smile.commons.StringBand;
import org.smile.strate.ActionConstants;
import org.smile.strate.i18n.I18nResource;
import org.smile.util.StringUtils;
/**
 * 提交按钮
 * @author 胡真山
 * @Date 2016年1月20日
 */
public class SubmitTag extends TagSupport{
	private String name;
	private String id;
	private String action;
	private String onclick;
	private String key;
	private String value;
	private boolean disable;
	private boolean hidden;
	private String css;
	
	
	@Override
	public int doEndTag() throws JspException {
		StringBand sb=new StringBand();
		sb.append("<input type=").appendDquote("submit");
		if(StringUtils.notEmpty(id)){
			sb.append(" id=").appendDquote(id);
		}
		if(StringUtils.notEmpty(action)){
			sb.append(" name=").appendDquote(ActionConstants.dynamicMethodFlag+action);
		}else if(StringUtils.notEmpty(name)){
			sb.append(" name=").appendDquote(name);
		}
		if(StringUtils.notEmpty(onclick)){
			sb.append(" onclick=").appendDquote(onclick);
		}
		sb.append(" value=");
		if(StringUtils.notEmpty(key)){
			Locale l=pageContext.getRequest().getLocale();
			value=I18nResource.message.getStringKeyDefault(l, key);
		}
		sb.appendDquote(value);
		
		if(disable){
			sb.append(" disabled");
		}
		
		if(hidden){
			sb.append(" style=").appendDquote("display:none;");
		}
		
		if(StringUtils.notEmpty(css)){
			sb.append(" class=").appendDquote(css);
		}
		sb.append("/>");
		
		try {
			pageContext.getOut().print(sb);
		} catch (IOException e) {
			throw new JspException(e);
		}
		return SKIP_BODY;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public void setOnclick(String onclick) {
		this.onclick = onclick;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public void setDisable(boolean disable) {
		this.disable = disable;
	}
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}
	public void setCss(String css) {
		this.css = css;
	}
	
	
	
}
