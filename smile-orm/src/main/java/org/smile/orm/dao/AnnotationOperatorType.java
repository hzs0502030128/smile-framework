package org.smile.orm.dao;

import java.util.HashMap;
import java.util.Map;

import org.smile.db.Db;
import org.smile.orm.xml.execut.BatchOperator;
import org.smile.orm.xml.execut.DeleteOperator;
import org.smile.orm.xml.execut.IOperator;
import org.smile.orm.xml.execut.InsertOperator;
import org.smile.orm.xml.execut.SelectOperator;
import org.smile.orm.xml.execut.UpdateOperator;


/**
 * 操作类型  预设了注解数据库操作类型的处理类
 * @author 胡真山
 * 2015年11月6日
 */
public class AnnotationOperatorType {
	
	private static Map<String,Class> operatorAnnMap=new HashMap<String,Class>();
	
	static{
		operatorAnnMap.put(Db.SELECT, SelectOperator.class);
		operatorAnnMap.put(Db.UPDATE, UpdateOperator.class);
		operatorAnnMap.put(Db.INSERT, InsertOperator.class);
		operatorAnnMap.put(Db.DELETE, DeleteOperator.class);
		operatorAnnMap.put(Db.BATCH, BatchOperator.class);
		
	}
	
	public static <T extends IOperator > Class<T> getOperatorClass(String type){
		return operatorAnnMap.get(type);
	}
}
