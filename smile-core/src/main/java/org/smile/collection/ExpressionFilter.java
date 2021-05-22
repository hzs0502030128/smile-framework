package org.smile.collection;

import java.beans.IntrospectionException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.smile.beans.converter.BeanException;
import org.smile.collection.CollectionUtils.Filter;
import org.smile.commons.SmileRunException;
import org.smile.expression.Context;
import org.smile.expression.DefaultContext;
import org.smile.expression.Engine;
import org.smile.expression.Expression;
import org.smile.expression.parser.ParseException;
import org.smile.log.LoggerHandler;
/***
 *  java object query langrange 
 * @author 胡真山
 *
 */
public class ExpressionFilter implements Filter<Object>, LoggerHandler{
	/**
	 * 返回每条记录的字段，如果是返回所有的字段，则这个属性可以不指定
	 */
	private Map<String,Object> parameters=new LinkedHashMap<String,Object>();
	
	private static Engine engine=new Engine();
	/**
	 * 条件过滤子句
	 */
	private String whereOql;
	
	private Expression condition;
	
	protected Context rootContext;
	
	public ExpressionFilter(String where){
		this.whereOql=where;
		this.rootContext=new DefaultContext();
	}
	/**
	 * 设置占位参数的值
	 */
	public void setParamter(String name,Object value){
		this.parameters.put(name,value);
	}
	
	public void addParamter(Object value){
		this.parameters.put(String.valueOf(parameters.size()),value);
	}
	/**
	 * 设置参数
	 * @param parameter
	 */
	public void setParamters(Object parameter){
		if(parameter instanceof Map){
			this.parameters=(Map)parameter;
		}else{
			this.parameters=new WrapBeanAsMap(parameter);
		}
		this.rootContext.setParameters(this.parameters);
	}
	
	/**准备
	 * @throws ParseException */
	public void prepared() throws ParseException{
		if(condition==null){
			this.condition=engine.parseExpression(whereOql);
			rootContext.setParameters(parameters);
		}
	}
	
	/**过滤一个对象是否满足条件*/
	@Override
	public boolean pass(Object value){
		rootContext.setRoot(value);
		return (Boolean) condition.evaluate(rootContext);
	}
	
	/**
	 * 查询结果
	 * @param srcList
	 * @return
	 * @throws JoqlParseException 
	 * @throws BeanException 
	 * @throws Exception 
	 * @throws IntrospectionException 
	 */
	public <T> List<T> listResult(Collection<T> srcList){
		List<T> list=new ArrayList<T>();
		Iterator<T> iter=srcList.iterator();
		try {
			prepared();
			while(iter.hasNext()){
				T value=iter.next();
				if(this.pass(value)){
					list.add(value);
				}
			}
		} catch (ParseException e) {
			throw new SmileRunException("解析语句失败"+whereOql,e);
		}
		return list;
	}
	/**
	 * 查询一个结果
	 * @param srcList
	 * @return
	 * @throws JoqlParseException
	 */
	public <T> T oneResult(Collection<T> srcList){
		Iterator<T> iter=srcList.iterator();
		try {
			prepared();
			while(iter.hasNext()){
				T value=iter.next();
				if(this.pass(value)){
					return value;
				}
			}
		} catch (ParseException e) {
			throw new SmileRunException("解析语句失败"+whereOql,e);
		}
		return null;
	}
	/***
	 * 删除list中的对象
	 * @param srcList
	 * @return
	 */
	public <T> List<T> deleteResult(Collection<T> srcList){
		List<T> result=new LinkedList<T>();
		Iterator<T> iter=srcList.iterator();
		try {
			prepared();
			while(iter.hasNext()){
				T value=iter.next();
				if(this.pass(value)){
					result.add(value);
					iter.remove();
				}
			}
		} catch (ParseException e) {
			throw new SmileRunException("解析语句失败"+whereOql,e);
		}
		return result;
	}
	
}
