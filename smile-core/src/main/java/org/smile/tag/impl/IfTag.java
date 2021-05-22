package org.smile.tag.impl;

import org.smile.commons.ann.Attribute;
import org.smile.tag.SimpleTag;
import org.smile.tag.Tag;

public class IfTag extends SimpleTag{
	
	static final String IF_ELSE_TEST="_current_ifelse_test_var";
	
	@Attribute(required=true)
	protected Boolean test;

	@Override
	protected void doTag() throws Exception {
		setIfElseSuccess(parent, test);
        if(test){
        	 invokeBody();
        }
	}
	
	/**
	 * 设置ifelse的结果 
	 * @param parent
	 * @param isSucess
	 * @param pageContext
	 */
	public void setIfElseSuccess(Tag parent,Boolean isSucess){
		if(parent!=null&&parent instanceof IfElseTag){
			((IfElseTag)parent).setSuccess(isSucess);
		}else{
			tagContext.setAttribute(IF_ELSE_TEST,isSucess);
		}
	}
	/**
	 * 获取当前if的值
	 * @param parent
	 * @return
	 */
	public Boolean getIfElseSuccess(Tag parent){
		if(parent!=null&&parent instanceof IfElseTag){
			return ((IfElseTag)parent).isSuccess();
		}else{
			Object result=tagContext.getAttribute(IF_ELSE_TEST);
			if(result!=null){
				return (Boolean)result;
			}
			return false;
		}
	}
}
