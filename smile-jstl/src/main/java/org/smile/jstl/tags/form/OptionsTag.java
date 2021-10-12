package org.smile.jstl.tags.form;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.smile.commons.Enum;
import org.smile.jstl.tags.core.DeclareSupport;
import org.smile.util.StringUtils;

/**
 * options标签
 * 
 * @author strive
 *
 */
public class OptionsTag extends TagSupport {
	/**
	 * 一个实例了com.chinasofti.hn.common.enumerate.Enum接口的 类名字符串
	 */
	private String classname;
	private Object data;
	private Object value;
	private boolean hasNull = true;

	@SuppressWarnings("unchecked")
	public int doStartTag() throws JspException {
		try {
			JspWriter out = pageContext.getOut();
			Map m;
			if (StringUtils.isEmpty(classname)) {
				m = DeclareSupport.parseToMap(data);
			} else {
				Enum e = (Enum) Class.forName(classname).newInstance();
				m = e.getDataMap();
			}
			Set keyset = m.keySet();
			Iterator iterator = keyset.iterator();
			StringBuffer str = new StringBuffer();
			if (hasNull) {
				str.append("<option value=\"\">--请选择--</option>");
			}
			while (iterator.hasNext()) {
				Object key = iterator.next();
				str.append("<option value='" + key + "'");
				if (String.valueOf(key).equals(String.valueOf(value))) {
					str.append(" selected ");
				}
				str.append(">" + m.get(key) + "</option>");
			}
			out.print(str);
		} catch (Exception e) {
			throw new JspTagException(e.getMessage());
		}
		return SKIP_BODY;
	}

	public String getClassname() {
		return classname;
	}

	public void setClassname(String classname) {
		this.classname = classname;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public boolean isHasNull() {
		return hasNull;
	}

	public void setHasNull(boolean hasNull) {
		this.hasNull = hasNull;
	}

}
