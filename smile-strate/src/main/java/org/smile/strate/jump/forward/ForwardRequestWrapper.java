package org.smile.strate.jump.forward;

import java.beans.PropertyDescriptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.smile.beans.BeanInfo;
import org.smile.commons.SmileRunException;
import org.smile.log.LoggerHandler;
import org.smile.strate.action.Action;

public class ForwardRequestWrapper extends HttpServletRequestWrapper implements LoggerHandler{
	
	protected Action action;
	
	public ForwardRequestWrapper(HttpServletRequest request,Action action) {
		super(request);
		this.action=action;
	}
	
	@Override
	public Object getAttribute(String name){
		Object value=getAttributeFormAction(name);
		if(value!=null){
			return value;
		}
		return super.getAttribute(name);
	}
	
	protected Object getAttributeFormAction(String name){
		BeanInfo beaninfo=BeanInfo.getInstance(action.getClass());
		if(beaninfo!=null){
			PropertyDescriptor pd=beaninfo.getPropertyDescriptor(name);
			if(pd!=null&&pd.getReadMethod()!=null){
				try {
					return pd.getReadMethod().invoke(action);
				} catch (Exception e) {
					throw new SmileRunException("get "+action+" property "+name+" error",e);
				}
			}
		}
		return null;
	}
}
