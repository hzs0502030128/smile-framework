package org.smile.beans.handler;

import ognl.Ognl;
import ognl.OgnlException;

import org.smile.beans.PropertyHandler;
import org.smile.beans.converter.BeanException;

public class OgnlPropertyHandler implements PropertyHandler<Object>{
	
	private static final OgnlPropertyHandler instance=new OgnlPropertyHandler();

	@Override
	public Object getExpFieldValue(Object target, String exp) throws BeanException {
		try {
			return Ognl.getValue(exp, target);
		} catch (OgnlException e) {
			throw new BeanException("获取 "+target+" 属性 "+exp+" fail ",e);
		}
	}

	@Override
	public void setExpFieldValue(Object target, String exp, Object value) throws BeanException {
		try {
			Ognl.setValue(exp, target,value);
		} catch (OgnlException e) {
			throw new BeanException("设置 "+target+" 属性 "+exp+" fail ",e);
		}
	}
	
	
	public static final OgnlPropertyHandler getInstance(){
		return instance;
	}
}
