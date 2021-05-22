package org.smile.dataset;

import java.util.Map;
import java.util.Set;

import org.smile.beans.BeanProperties;
import org.smile.beans.FieldDeclare;
import org.smile.beans.PropertiesGetter;
import org.smile.beans.PropertyHandler;
import org.smile.beans.PropertyHandlers;
import org.smile.beans.converter.BeanException;
import org.smile.beans.handler.MapPropertyHandler;
import org.smile.commons.NotImplementedException;
import org.smile.commons.SmileRunException;
import org.smile.expression.AbstractContext;
import org.smile.expression.Engine;

public class DataSourceContext extends AbstractContext{
	/**根对象内容*/
	private Object rootObj;
	
	public DataSourceContext(Object root){
		this.rootObj=root;
		this.engine=Engine.getInstance();
	}
	
	@Override
	public Object get(String exp) {
		if(this.rootObj==null){
			return null;
		}
		if(this.rootObj instanceof PropertiesGetter){
			int idx=exp.indexOf('.');
			if(idx>0){
				Object obj=((PropertiesGetter) this.rootObj).getValue(exp.substring(0, idx));
				if(obj==null){
					return null;
				}
				try {
					return new FieldDeclare(obj.getClass()).getExpValue(obj,BeanProperties.NORAL_CAN_NO_PROPERTY, exp.substring(idx+1));
				} catch (BeanException e) {
					throw new SmileRunException(e);
				}
			}
			return ((PropertiesGetter) this.rootObj).getValue(exp);
		}else{
			PropertyHandler handler= PropertyHandlers.getHanlder(rootObj.getClass(), false);
			try {
				return handler.getExpFieldValue(rootObj, exp);
			} catch (BeanException e) {
				throw new SmileRunException(e);
			}
		}
	}
	
	

	@Override
	public void set(String exp, Object value) {
		PropertyHandler handler= PropertyHandlers.getHanlder(rootObj.getClass(), false);
		try {
			handler.setExpFieldValue(rootObj, exp,value);
		} catch (BeanException e) {
			throw new SmileRunException(e);
		}
	}

	@Override
	public void setRoot(Object rootObj) {
		this.rootObj=rootObj;
	}

	@Override
	protected Object getFieldValue(Map targetMap, String exp) {
		try {
			return MapPropertyHandler.DEFAULT.getExpFieldValue(targetMap, exp);
		} catch (BeanException e) {
			throw new SmileRunException(e);
		}
	}
	
	public Object evaluate(String expression){
		return this.engine.evaluate(this, expression);
	}

	@Override
	public Set<String> keys() {
		throw new NotImplementedException();
	}

}
