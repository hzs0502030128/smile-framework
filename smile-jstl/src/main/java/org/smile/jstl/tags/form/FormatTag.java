package org.smile.jstl.tags.form;

import java.sql.Date;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.smile.math.NumberUtils;
import org.smile.util.DateUtils;
import org.smile.util.StringUtils;
/**
 * 格式 化标签
 * @author 胡真山
 * 2015年10月15日
 */
public class FormatTag extends TagSupport {
	/**
	 * 源值
	 */
	protected Object value;
	/**
	 * 格式
	 */
	protected String format;
	/***
	 * 默认的日期格式 
	 */
	protected static final String DEFAULT_DATE_FORMAT="yyyy-MM-dd HH:mm:ss";
	/***
	 * 默认的数字格式 
	 */
	protected static final String DEFAULT_NUMBER_FORMAT="####.###";
	
	@Override
	public int doStartTag() throws JspException {
		String result=null;
		if(value instanceof Number){
			if(StringUtils.isEmpty(format)){
				result= NumberUtils.format((Number)value,DEFAULT_NUMBER_FORMAT);
			}else{
				result= NumberUtils.format((Number)value,format);
			}
		}else if(value instanceof Date){
			if(StringUtils.isEmpty(format)){
				result=DateUtils.formatDate((Date)value, DEFAULT_DATE_FORMAT);
			}else{
				result=DateUtils.formatDate((Date)value, format);
			}
		}else if(value instanceof String){
			try{
				Double number=Double.parseDouble((String)value);
				if(StringUtils.isEmpty(format)){
					result= NumberUtils.format(number);
				}else{
					result= NumberUtils.format(number,format);
				}
			}catch(Exception e){}
		}else{
			result=String.valueOf(value);
		}
		try{
			pageContext.getOut().print(result);
		}catch(Exception e){
			throw new JspException(e);
		}
		return SKIP_BODY;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}
	
	
}
