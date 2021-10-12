package org.smile.jstl.tags.form;


import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.smile.commons.Enum;
import org.smile.jstl.tags.core.DeclareSupport;
import org.smile.util.RegExp;
import org.smile.util.StringUtils;
/**
 * 这是一个多个checkbox的标签 
 * 数据保存格式为 田边;路边:其它
 * @author strive
 *
 */
public class CheckBoxsTag extends TagSupport{
	/**
	 * 标签ID
	 */
	private String id;
	/**
	 * 指定class 数据来源  此类必须实现Enum接口
	 */
	private String classname;
	/**
	 * MAP 数据来源
	 */
	private Object data;
	/**
	 * 标签的name 表单中也是用此name 来提交数据
	 */
	private String name;
	/**
	 * 指定标签的值
	 */
	private Object value;
	/**
	 * 是否有其它输入框
	 */
	private String others;
	/**
	 * 每一列显示checkbox的个数
	 */
	private String cols;
	
	public int doStartTag() throws JspException {    
	    try {
	      RegExp reg=new RegExp("[:]+");
	      String[] strs=value==null?null:reg.split((String)value);
	      String other=null;
	      String valueStr=(String)value;
	      if(strs!=null)
	      {
	    	  String[] l=reg.find((String)value);
	    	  if(l==null){
	    		  valueStr=strs[0];
	    	  }else{
		    	  if(strs.length>1)
		    	  {
		    		  valueStr=strs[0];
		    		  other=strs[1];
		    	  }else{
		    		  other=strs[0];
		    	  }
	    	  }
	      }
	      String[] valueArray=valueStr==null?null:new RegExp("[;]+").split(valueStr);
	      JspWriter out = pageContext.getOut();      
	     
	      Map m=null;
	      if(data==null){
	    	  if(classname==null){
	    		  throw new JspException("data property must not null ");
	    	  }
	    	  Enum e=(Enum) Class.forName(classname).newInstance();
	    	  m=e.getDataMap();
	      }else if(data!=null){
	    	  m=DeclareSupport.parseToMap(data);
	      }
	      Set keyset=m.keySet();
	      Iterator iterator=keyset.iterator();
	      if(StringUtils.isEmpty(id))
	      {
	    	  id="checkbox_"+name;
	      }
	      StringBuffer sb=new StringBuffer();
	      sb.append("<input id='"+id+"' type='hidden' name='"+name+"' value='");
	      if(value==null||"null".equals(value)){
	    	  sb.append("");
	      }else{
	    	  sb.append(value);
	      }
	      sb.append("'>\r\n");
	      int colCount=0;
	      try{
	    	  colCount=Integer.parseInt(cols);
	      }catch(Exception ee)
	      {}
	      int i=0;
	      while(iterator.hasNext())
	      {
	    	  i++;
	    	  Object key=iterator.next();
	    	  sb.append("<input name='checkbox_option_"+name+"' type='checkbox' value='"+key+"'");
	    	  if(isChecked(valueArray,(String)key))
	    	  {
	    		  sb.append(" checked ");
	    	  }
	    	  sb.append(" onclick='");
	    	  sb.append("var checkboxs=document.getElementsByName(\"checkbox_option_"+name+"\");");
	    	  sb.append("var hiddenInput=document.getElementById(\""+id+"\");");
	    	  sb.append("var otherInput=document.getElementById(\"checkbox_option_other_"+name+"\");");
	    	  sb.append("var valueStr=\"\";");
	    	  sb.append("for(var i=0;i<checkboxs.length;i++){if(checkboxs[i].checked){valueStr+=checkboxs[i].value+\";\";}}if(valueStr!=\"\"){valueStr=valueStr.substring(0,valueStr.length-1)}if(otherInput&&otherInput.value!=\"\"){valueStr+=\":\"+otherInput.value}hiddenInput.value=valueStr;'");
	    	  sb.append(">"+m.get(key)+"\r\n");
	    	  if(colCount!=0&&i%colCount==0)
	    	  {
	    		  sb.append("<br>");
	    	  }
	    	
	      }
	      if(others!=null&&others.equals("true"))
	      {
	    	  sb.append(" 其它:<input type='text' id='checkbox_option_other_"+name+"' value='"+(other==null?"":other)+"' ");
	    	  sb.append(" onblur='");
	    	  sb.append("var checkboxs=document.getElementsByName(\"checkbox_option_"+name+"\");");
	    	  sb.append("var hiddenInput=document.getElementById(\""+id+"\");");
	    	  sb.append("var otherInput=document.getElementById(\"checkbox_option_other_"+name+"\");");
	    	  sb.append("var valueStr=\"\";");
	    	  sb.append("for(var i=0;i<checkboxs.length;i++){if(checkboxs[i].checked){valueStr+=checkboxs[i].value+\";\";}}if(valueStr!=\"\"){valueStr=valueStr.substring(0,valueStr.length-1)}if(otherInput&&otherInput.value!=\"\"){valueStr+=\":\"+otherInput.value}hiddenInput.value=valueStr;'");
	    	  sb.append(">");
	      }
	      out.print(sb);
	    } catch (Exception e) {
	    	throw new JspException("checkboxs tag create error ",e);
	    }
	    return SKIP_BODY;
	 }
	private boolean isChecked(String[] valueArray,String value)
	{
		if(valueArray==null||value==null) return false;
		try{
			for(String s:valueArray)
			{
				if(s.equals(value))
				{
					return true;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}
	public String getClassname() {
		return classname;
	}

	public void setClassname(String classname) {
		this.classname = classname;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
		this.value=value;
	}

	public String getOthers() {
		return others;
	}

	public void setOthers(String others) {
		this.others = others;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCols() {
		return cols;
	}
	public void setCols(String cols) {
		this.cols = cols;
	}
}
