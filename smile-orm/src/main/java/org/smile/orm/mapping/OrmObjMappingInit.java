package org.smile.orm.mapping;

import org.smile.log.LoggerHandler;
import org.smile.orm.mapping.flag.TableFlag;

public class OrmObjMappingInit implements LoggerHandler{
	
	private String mapperName;
	
	private OrmObjMapping ormObjMapping =null;
	
	private Class<?> clazz;
	/**如果是对表的映射 ，则需要是哪种类型去实例化*/
	private Class<? extends OrmTableMapping> tableMapperClass;
	/**
	 * 初始化对象映射  
	 * @param clazz  注解配置了属性与数据库字段对应
	 * @throws MappingException 
	 */
	public OrmObjMappingInit(Class<?> clazz,Class<? extends OrmTableMapping> tableMapperClass){
		this.clazz=clazz;
		this.tableMapperClass=tableMapperClass;
	}
	
	public boolean init() throws MappingException{
		TableFlag tableFlag=OrmObjMapping.flagHandler.getTableFlag(clazz);
		if(tableFlag!=null){
			if(tableFlag.isTable()){
				try{
					ormObjMapping =tableMapperClass.newInstance();
				}catch(Exception e){
					throw new MappingException(e.getMessage());
				}
			}else{
				ormObjMapping =new OrmObjMapping();
			}
			mapperName=tableFlag.getName();
			ormObjMapping.initType(clazz);
			logger.info("init mapper success type "+ ormObjMapping.getClass().getName()+" "+ ormObjMapping.getRawClass().getName()+"-->"+ ormObjMapping.getName());
			return true;
		}
		return false;
	}
	
	public String getMapperName() {
		return mapperName;
	}
	
	public OrmObjMapping getOrmObjMapping() {
		return ormObjMapping;
	}
}
