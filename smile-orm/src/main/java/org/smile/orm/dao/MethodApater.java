package org.smile.orm.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.smile.beans.converter.BeanException;
import org.smile.orm.OrmApplication;
import org.smile.orm.executor.BatchMethod;
import org.smile.orm.executor.DeleteMethod;
import org.smile.orm.executor.ExecuteMethod;
import org.smile.orm.executor.InsertMethod;
import org.smile.orm.executor.MappedOperator;
import org.smile.orm.executor.SelectMethod;
import org.smile.orm.executor.UpdateMethod;
import org.smile.orm.xml.execut.BatchOperator;
import org.smile.orm.xml.execut.DeleteOperator;
import org.smile.orm.xml.execut.InsertOperator;
import org.smile.orm.xml.execut.SelectOperator;
import org.smile.orm.xml.execut.UpdateOperator;

public  class MethodApater {
	
	private Map<Class,ExecuteMethod> apterMap=new HashMap<Class, ExecuteMethod>();
	
	private OrmApplication application;
	
	public MethodApater(OrmApplication application){
		this.application=application;
		ExecuteMethod method=application.plugin(new SelectMethod());
		apterMap.put(SelectOperator.class,method);
		method=application.plugin(new UpdateMethod());
		apterMap.put(UpdateOperator.class,method);
		method=application.plugin(new BatchMethod());
		apterMap.put(BatchOperator.class,method);
		method=application.plugin(new InsertMethod());
		apterMap.put(InsertOperator.class,method);
		method=application.plugin(new DeleteMethod());
		apterMap.put(DeleteOperator.class,method);
	}
	
	public Object handler(MappedOperator operator,Object param) throws SQLException{
		ExecuteMethod method=apterMap.get(operator.getOperator().getClass());
		try{
			return method.execute(application.getExecutor(),operator, param);
		}catch(BeanException e){
			throw new SQLException(operator.getId()+",执行失败",e);
		}
	}
}
