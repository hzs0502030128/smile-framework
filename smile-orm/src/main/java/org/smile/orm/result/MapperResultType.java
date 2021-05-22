package org.smile.orm.result;

import org.smile.db.handler.RowHandler;
import org.smile.orm.executor.FieldMapperRowHandler;
import org.smile.orm.mapping.OrmMapping;
import org.smile.orm.mapping.OrmObjMapping;
/**
 * 映射返回类型
 * @author 胡真山
 *
 */
public class MapperResultType extends BaseResultType{
	/**对象映射*/
	private OrmObjMapping resultMapper;
	
	public MapperResultType(Class type,OrmObjMapping mapper){
		this.type=type;
		this.resultMapper=mapper;
	}

	public OrmObjMapping getResultMapper() {
		return resultMapper;
	}

	@Override
	public Class getGenericType() {
		return resultMapper.getRawClass();
	}

	@Override
	public RowHandler createRowHandler() {
		return new FieldMapperRowHandler(getResultMapper());
	}
	
	
}
