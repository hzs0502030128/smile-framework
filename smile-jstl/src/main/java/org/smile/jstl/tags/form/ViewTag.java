package org.smile.jstl.tags.form;

import java.io.IOException;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.smile.collection.CollectionUtils;
import org.smile.jstl.tags.JSONParseSupport;

public class ViewTag extends SimpleTagSupport{
	private Object value;
	private Object data;
	private Object defaultValue;

	public void setValue(Object value) {
		this.value = value;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
	public void setDefault(Object value){
		this.defaultValue=value;
	}


	@Override
	public void doTag() throws JspException {
		Map<Object,Object> dataMap=JSONParseSupport.parseToMap(data);
		if(CollectionUtils.notEmpty(dataMap)){
			for(Map.Entry<Object, Object> entry:dataMap.entrySet()){
				if(value!=null&&String.valueOf(entry.getKey()).equals(String.valueOf(value))){
					defaultValue=entry.getValue();
				}
			}
		}
		JspWriter out=getJspContext().getOut();
		try {
			if(defaultValue!=null){
				out.print(defaultValue);
			}
		} catch (IOException e) {
			throw new JspException(e);
		}
	}
	
	protected void reset(){
		defaultValue=null;
		value=null;
	}
}
