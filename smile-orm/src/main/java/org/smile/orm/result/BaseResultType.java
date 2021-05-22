package org.smile.orm.result;

import java.sql.Date;
import java.sql.SQLException;
import java.util.Map;

import org.smile.db.handler.LikeBeanRowHandler;
import org.smile.db.handler.MapRowHandler;
import org.smile.db.handler.OneFieldRowHandler;
import org.smile.db.handler.RowHandler;
import org.smile.db.sql.BoundSql;
import org.smile.db.sql.SQLRunner;
import org.smile.reflect.ClassTypeUtils;

/**
 * 方法反回类型
 * @author 胡真山
 * 2015年10月29日
 */
public abstract class BaseResultType implements ResultType{
	
	/**反回的类型*/
	protected Class type;
	/**
	 * 是否是单字段类型 ，如String Integer 
	 */
	protected volatile boolean isOneFieldType=false;
	/**
	 * 是否是boolean类型
	 */
	protected volatile boolean isBooleanType=false;
	
	/**
	 * 是否是单字段类型
	 * @return
	 */
	@Override
	public boolean isOneFieldType(){
		return isOneFieldType;
	}
	
	
	@Override
	public Class getType(){
		return this.type;
	}
	/**
	 * 是否是单对象类型 
	 * @return
	 */
	@Override
	public boolean isSingleObj(){
		return false;
	}

	@Override
	public boolean isBooleanType() {
		return isBooleanType;
	}


	@Override
	public void onInit() {
		initIsBooleanType();
		initIsOneFieldType();
	}
	
	/**
	 * 初始化是否是boolean类型的返回
	 */
	private void initIsBooleanType(){
		Class returnType=getGenericType();
		if(returnType==boolean.class||returnType==Boolean.class){
			this.isBooleanType=true;
		}else{
			this.isBooleanType=false;
		}
	}
	/**
	 * 实始化是否是单字段类型返回值
	 */
	private void initIsOneFieldType(){
		Class genericType=getGenericType();
		if(ClassTypeUtils.isBasicType(genericType)){
			isOneFieldType= true;
		}else if(ClassTypeUtils.isBasicObjType(genericType)){
			isOneFieldType= true;
		}else if(genericType==String.class){
			isOneFieldType=true;
		}else if(Date.class.isAssignableFrom(genericType)){
			isOneFieldType=true;
		}else if(Number.class.isAssignableFrom(genericType)){
			isOneFieldType=true;
		}
	}


	@Override
	public RowHandler createRowHandler() {
		if (Map.class.isAssignableFrom(getGenericType())) {
			return new MapRowHandler(getGenericType());
		} else if (isOneFieldType()) {
			return OneFieldRowHandler.instance(getGenericType());
		} else {
			return new LikeBeanRowHandler(getGenericType());
		}
	}

	@Override
	public Object executeQuery(SQLRunner runner, BoundSql boundSql) throws SQLException {
		return runner.query(boundSql);
	}
}
