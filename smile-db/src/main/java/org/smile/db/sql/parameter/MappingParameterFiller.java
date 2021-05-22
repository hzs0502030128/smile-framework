package org.smile.db.sql.parameter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Map;

import org.smile.beans.converter.BeanException;
import org.smile.db.SqlRunException;
import org.smile.function.Function;
/***
 * 参数设值
 * @author 胡真山
 * 2015年8月8日
 */
public abstract class MappingParameterFiller extends AbstractParameterFiller{
	
	protected ParameterMapping parameterMapping;
	
	protected Map<String,Object> paramerMap;
	
	protected MappingParameterFiller(){}
	
	public MappingParameterFiller(ParameterMapping mapping){
		this.parameterMapping=mapping;
	}
	/**
	 * 把一个对象设置到ps中  
	 * @param ps
	 * @param value
	 * @throws Exception
	 */
	@Override
	public final void fillObject(PreparedStatement ps,Object value) throws SQLException{
		LinkedList<Parameter> paramemters= parameterMapping.values();
		logger.debug(paramemters);
		int index=1;
		for(Parameter pm:paramemters){
			Object columnVal=getParameterValue(pm,value);
			fillObject(ps, index++, columnVal);
		}
	}
	
	public final void fillBatchObject(PreparedStatement ps,Object value) throws SQLException{
		int index=1;
		for(Parameter pm:parameterMapping.values()){
			Object columnVal=getParameterValue(pm.getName(),value);
			fillObject(ps, index++, columnVal);
		}
	}
	
	public void setParamerMap(Map<String, Object> paramerMap) {
		if(paramerMap instanceof BatchParameterMap){
			this.paramerMap = paramerMap;
		}
		
	}
	/***
	 * 
	 * @param p
	 * @param value
	 * @return
	 * @throws SQLException
	 */
	protected final Object getParameterValue(Parameter p,Object value) throws SQLException{
		//如果在解析sql已经获取到参数的值了
		if(p.getValue()!=null){
			return p.getValue();
		}
		//如果参数值没有,从填充对象中按属性获取
		Object result=getParameterValue(p.getName(), value);
		Function function=p.getFunction();
		if(function==null){
			return result;
		}else{
			return function.getFunctionValue(result);
		}
	}
	/**
	 * 从填充对象中获取属性值
	 * @param name 属性名称
	 * @param value 参数填充对象
	 * @return
	 * @throws BeanException
	 */
	protected abstract Object getParamterValueForm(String name,Object value) throws BeanException;
	/**
	 * 
	 * @param name
	 * @return
	 */
	protected Object getValueFormBatchMap(String name){
		if(paramerMap!=null){
			Object result=paramerMap.get(name);
			if(result!=null){
				return result;
			}
		}
		return null;
	}
	
	protected final Object getParameterValue(String name,Object value){
		try {
			//先从batchMap中获取
			Object result=getValueFormBatchMap(name);
			if(result!=null){
				return result;
			}
			return getParamterValueForm(name, value);
		} catch (BeanException e) {
			throw new SqlRunException("get "+value+" parameter "+name+" has a error",e);
		}
	}
}
