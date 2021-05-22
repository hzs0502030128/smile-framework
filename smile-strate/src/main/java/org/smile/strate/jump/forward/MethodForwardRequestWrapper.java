package org.smile.strate.jump.forward;

import javax.servlet.http.HttpServletRequest;

import org.smile.beans.PropertyHandler;
import org.smile.beans.PropertyHandlers;
import org.smile.beans.converter.BeanException;
import org.smile.commons.SmileRunException;
import org.smile.strate.action.Action;

public class MethodForwardRequestWrapper extends ForwardRequestWrapper{
	protected Object methodResult;
	protected PropertyHandler handler;
	public MethodForwardRequestWrapper(HttpServletRequest request, Action action,Object methodResult) {
		super(request, action);
		this.methodResult=methodResult;
		if(methodResult!=null){
			this.handler=PropertyHandlers.getHanlder(methodResult.getClass(),false);
		}
	}

	@Override
	public Object getAttribute(String name){
		Object value=getAttributeFormMethodResult(name);
		if(value!=null){
			return value;
		}
		return super.getAttribute(name);
	}
	
	protected Object getAttributeFormMethodResult(String name){
		if(this.methodResult==null){
			return null;
		}
		try {
			Object value=handler.getExpFieldValue(methodResult, name);
			if(value!=null){
				return value;
			}
		} catch (BeanException e) {
			throw new SmileRunException("get attribute "+name,e);
		}
		return null;
	}
}
