package org.smile.db.sql.parameter;

import java.util.Iterator;
import java.util.LinkedList;

import org.smile.util.StringUtils;

public class ParameterMapping implements Iterable<Parameter>{
	/**
	 * 映射的对象类型
	 */
	private Class type;
	/***
	 * 映射的参数集合
	 * 对象的属性
	 */
	private LinkedList<Parameter> params=new LinkedList<Parameter>();
	
	public ParameterMapping(Class type){
		this.type=type;
	}
	
	public Class getType(){
		return type;
	}

	@Override
	public Iterator<Parameter> iterator() {
		return params.iterator();
	}
	/**
	 * 添加一个参数映射
	 * @param name 参数名
	 * @param value 参数值
	 */
	public void addParameter(String name,Object value){
		params.add(new Parameter(name, value));
	}
	
	public void addParameter(Parameter p){
		params.add(p);
	}
	
	public LinkedList<Parameter> values(){
		return params;
	}

	@Override
	public String toString() {
		return StringUtils.join(params,',');
	}
	
	
}
