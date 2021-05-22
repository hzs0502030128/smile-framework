package org.smile.expression;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.smile.beans.PropertyHandler;
import org.smile.beans.converter.BeanException;
import org.smile.beans.handler.MapPropertyHandler;
import org.smile.collection.WrapBeanAsMap;
import org.smile.commons.SmileRunException;
import org.smile.expression.Engine.FunctionKey;
import org.smile.function.Function;

public abstract class AbstractContext implements Context{
	/**除法除不尽时保留位数*/
	protected int divideScale=8;
	/***参数集合*/
	protected Map parameters;
	/**当前的表达式引擎*/
	protected Engine engine;
	/**注册函数集*/
	protected volatile Map<FunctionKey,Function> functions; 
	
	protected PropertyHandler propertyHandler=MapPropertyHandler.DEFAULT;
	/***
	 * 从map中获取一个表达式的值
	 * @param targetMap
	 * @param exp
	 * @return
	 */
	protected abstract Object getFieldValue(Map targetMap,String exp);

	@Override
	public Object getParameter(String param) {
		return getFieldValue(parameters, param);
	}

	@Override
	public void setParameters(Object params) {
		if(params!=null){
			this.parameters=wrapToMap(params);
		}
	}
	
	protected Map wrapToMap(Object value){
		if(value instanceof Map){
			return (Map) value;
		}else{
			return wrapBeanRoot(value);
		}
	}
	/**
	 * 对javabean类型进行包装
	 * @param value
	 * @return
	 */
	protected Map wrapBeanRoot(Object value){
		return new WrapBeanAsMap(value);
	}


	@Override
	public Function getFunction(FunctionKey name) {
		Function f=null;
		if(this.functions!=null){
			f=functions.get(name);
		}
		if(f==null){
			if(this.engine==null){
				return null;
			}
			return engine.getFunction(name);
		}
		return f;
	}
	
	@Override
	public synchronized void registFunction(Function f){
		if(this.functions==null){
			this.functions=new HashMap<FunctionKey,Function>();
		}
		this.functions.put(new FunctionKey(f.getName(),f.getSupportArgsCount()), f);
	}
	
	

	public void setEngine(Engine engine) {
		this.engine = engine;
	}
	/***
	 * 是否捆绑了引擎
	 * @return
	 */
	@Override
	public boolean isBinddEngin(){
		return this.engine!=null;
	}

	@Override
	public void registFunctions(Collection<Function> fs) {
		for(Function f:fs){
			registFunction(f);
		}
	}

	@Override
	public void onCompareSymbol(Object result, Expression left, Object leftValue, Expression right, Object rightValue) {
		//do method
	}

	@Override
	public int divideScale() {
		return divideScale;
	}

	@Override
	public void divideScale(int scale) {
		this.divideScale=scale;
	}
	
	@Override
	public void setParameter(String name, Object value) {
		if(this.parameters==null){
			this.parameters=new HashMap();
		}
		try {
			propertyHandler.setExpFieldValue(this.parameters, name, value);
		} catch (BeanException e) {
			throw new SmileRunException("set parameter name "+name,e);
		}
	}
	
	@Override
	public boolean canExecuteCode() {
		if(this.engine!=null){
			this.engine.isExecuteCode();
		}
		return true;
	}
}
