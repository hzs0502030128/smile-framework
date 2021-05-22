package org.smile.db.sql.parameter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;
/**
 * 映射参数处理器
 * @author 胡真山
 */
public class MappingParameterHandler implements ParameterHandler {
	
	public MappingParameterFiller filler;
	
	public  MappingParameterHandler(Object firstParam,ParameterMapping list){
		if(firstParam instanceof Map){
			this.filler=new MapParameterFiller(list);
		}else if(firstParam instanceof Number){
			this.filler=new BasicObjectParameterFiller(list);
		}else if(firstParam instanceof String){
			this.filler=new BasicObjectParameterFiller(list);
		}else if(firstParam instanceof Date){
			this.filler=new BasicObjectParameterFiller(list);
		}else{
			this.filler=new BeanParameterFiller(list);
		}
	}
	
	@Override
	public void setBatchMap(Map batchMap){
		this.filler.setParamerMap(batchMap);
	}
	
	@Override
	public void fillObject(PreparedStatement ps,Object value) throws SQLException{
		this.filler.fillObject(ps, value);
	}
	
	@Override
	public void fillBatchObject(PreparedStatement ps,Object value) throws SQLException{
		this.filler.fillBatchObject(ps, value);
	}
	
	@Override
	public Object getValue(Object value,String name){
		return this.filler.getParameterValue(name, value);
	}
	
}
