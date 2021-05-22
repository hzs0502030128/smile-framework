package org.smile.beans.handler;

import org.smile.beans.BeanProperties;
import org.smile.beans.FieldDeclare;
import org.smile.beans.PropertyHandler;
import org.smile.beans.converter.BeanException;

public abstract class DeclarePropertyHandler<T> implements PropertyHandler<T>{
	
	protected BeanProperties properties;
	
	protected FieldDeclare<Object> declare;
	
	@Override
	public Object getExpFieldValue(T target, String exp) throws BeanException {
		return declare.getExpValue(target, properties, exp);
	}

	@Override
	public void setExpFieldValue(T target, String exp, Object value) throws BeanException {
		declare.setExpValue(target,properties, exp,value);
	}
}
