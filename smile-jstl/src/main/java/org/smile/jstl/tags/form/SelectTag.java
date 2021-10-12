package org.smile.jstl.tags.form;

import java.io.IOException;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import org.smile.beans.converter.BeanException;
import org.smile.collection.CollectionUtils;
import org.smile.commons.StringBand;
import org.smile.jstl.tags.JSONParseSupport;
import org.smile.jstl.tags.PropertySupport;
import org.smile.util.StringUtils;

public class SelectTag extends DynamicAttributesTag{
	/**标签id*/
	private String id;
	/**标签名称 表单提交*/
	private String name;
	/**当前选中的值*/
	private Object value;
	/**数据来源*/
	private Object data;
	/**是否跳过为空的值*/
	private boolean skipNull=false;

	public void setName(String name) {
		this.name = name;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public int doStartTag() throws JspException {
		StringBand html=new StringBand();
		html.append("<select ");
		if(id!=null){
			html.append(" id='").append(id).append("'");
		}
		if(name!=null){
			html.append(" name='").append(name).append("'");
		}
		html.append(getDynAttrubiteHtml());
		html.append(">");
		Map<Object,Object> dataMap=JSONParseSupport.parseToMap(data);
		if(CollectionUtils.notEmpty(dataMap)){
			if(value==null){
				try {//没有指定值的时候自动从属性中获取
					value=PropertySupport.getPropertyValue(name, pageContext);
				} catch (BeanException e) {}
			}
			for(Map.Entry<Object, Object> entry:dataMap.entrySet()){
				if(skipNull&&StringUtils.isNull(entry.getKey())){
					continue;
				}
				html.append("<option value='").append(entry.getKey()).append("' ");
				if(value!=null&&String.valueOf(entry.getKey()).equals(String.valueOf(value))){
					html.append(" selected ");
				}
				html.append(">").append(entry.getValue()).append("</option>");
			}
		}
		JspWriter out=pageContext.getOut();
		try {
			out.print(html.toString());
		} catch (IOException e) {
			throw new JspException(e);
		}
		return EVAL_BODY_INCLUDE;
	}

	@Override
	public int doEndTag() throws JspException {
		JspWriter out=pageContext.getOut();
		try {
			out.print("</select>");
		} catch (IOException e) {
			throw new JspException(e);
		}
		reset();
		return super.doEndTag();
	}

	@Override
	protected void reset() {
		super.reset();
		this.value=null;
		this.id=null;
		this.name=null;
	}

	public void setSkipNull(boolean shipNull) {
		this.skipNull = shipNull;
	}
	
	
}
