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
/**
 * EnumJSON标签
 * @author strive
 *
 */
public class EnumJSONTag extends TagSupport {
	/**
	 * com.chinasofti.hn.common.enumerate.Enum
	 * 类名字符
	 */
	private String classname;
	
	private Object data;
	
	@SuppressWarnings("unchecked")
	public int doStartTag() throws JspException {    
		
	    try {
	      JspWriter out = pageContext.getOut();  
	      Map m=null;
	      if(classname==null||"".equals(classname)){
	    	 m=DeclareSupport.parseToMap(data); 
	      }else{
		      Enum e=(Enum) Class.forName(classname).newInstance();
		      m=e.getDataMap();
	      }
	      Set keyset=m.keySet();
	      Iterator iterator=keyset.iterator();
	      StringBuffer jsonStr=new StringBuffer("[");
	      int i=0;
	      while(iterator.hasNext())
	      {
	    	  Object key=iterator.next();
	    	  jsonStr.append("{value:'").append(key).append("',text:'").append(m.get(key)).append("'}");
	    	  if(i<keyset.size()-1){
	    		  jsonStr.append(",");
	    	  }
	    	  i++;
	      }
	      jsonStr.append("]");
	      out.print(jsonStr);
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
	
}
