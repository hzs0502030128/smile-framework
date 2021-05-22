package org.smile.orm;

import java.io.File;

import org.smile.annotation.AnnotationUtils;
import org.smile.commons.ann.Dao;
import org.smile.log.LoggerHandler;
import org.smile.orm.ann.Mapper;
import org.smile.orm.dao.DaoMapper;
import org.smile.orm.xml.execut.MapperXml;
import org.smile.reflect.ClassTypeUtils;
import org.smile.util.StringUtils;
/**
 * Mapper映射生新加载 
 * 	此功能用于对mapper的热部署加载
 * @author 胡真山
 *
 */
public class MapperReloader implements LoggerHandler{
	
	OrmApplication application;
	
	protected MapperReloader(OrmApplication application){
		this.application=application;
	}
	/**
	 * 重新加载mapper映射对象
	 * @param file
	 * @param clazz
	 * @param mapper
	 */
	protected  boolean reloadXmlCreateMapper(File file,Class<?> clazz,DaoMapper mapper){
		if(file!=null){
			long updateTime=file.lastModified();
			if(updateTime!=mapper.getXmlUpdateTime()){
				reloadCreateMapper(clazz, mapper);
				logger.debug("dao mapper "+clazz+" xml文件重新加载成功:"+file.getName());
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 重新加载mapper映射对象
	 * @param file
	 * @param clazz
	 * @param mapper
	 */
	protected  boolean reloadClassCreateMapper(File file,Class<?> clazz,DaoMapper mapper){
		if(file!=null){
			long updateTime=file.lastModified();
			if(updateTime!=mapper.getClassUpdateTime()){
				reloadCreateMapper(clazz, mapper);
				logger.debug("dao mapper "+clazz+" class注解重新加载成功:"+file.getName());
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 重新加载创建mapper  
	 * 把部分属性替换到原mapper中
	 * @param clazz
	 * @param mapper  原mapper
	 */
	private  void reloadCreateMapper(Class<?> clazz,DaoMapper mapper){
		DaoMapper newMapper=loadCreateMapper(clazz);
		if(newMapper!=null) {
			//替换属性
			mapper.replaceFromOther(newMapper);
		}
	}
	
	/**
	 * 加载DaoMapper 如果没有注解返回null
	 * @param clazz
	 * @return
	 */
	public DaoMapper loadCreateMapper(Class<?> clazz) {
		DaoMapper daoMapper=null;
		Mapper mapperAnn=AnnotationUtils.getAnnotation(clazz,Mapper.class);
		if(mapperAnn!=null) {
			daoMapper=createDaoMapper(clazz, mapperAnn);
		}else {
			Dao daoAnn=AnnotationUtils.getAnnotation(clazz,Dao.class);
			if(daoAnn!=null) {
				daoMapper=createDaoMapper(clazz, daoAnn);
			}
		}
		if(daoMapper!=null) {
			daoMapper.initResultMapper();
		}
		return daoMapper;
	}
	
	/**
	 * 创建daoMap接口的映射对象
	 * @param clazz
	 * @param daoAnn
	 * @return
	 */
	protected  DaoMapper createDaoMapper(Class<?> clazz,Dao daoAnn){
		String daoName=daoAnn.name();
		if(StringUtils.isEmpty(daoName)){
			daoName=ClassTypeUtils.getFirstCharLowName(clazz);
		}
		MapperXml xmlInfo=application.xmlHelper.getMapperXmlInfo(clazz);
		DaoMapper mapper=new DaoMapper(application,xmlInfo);
		mapper.setName(daoName);
		mapper.setDaoClass(clazz);
		mapper.doInit();
		return mapper;
	}
	
	/**
	 * 创建daoMap接口的映射对象
	 * @param clazz
	 * @param daoAnn
	 * @return
	 */
	protected  DaoMapper createDaoMapper(Class<?> clazz,Mapper mapperAnn){
		String daoName=mapperAnn.name();
		if(StringUtils.isEmpty(daoName)){
			daoName=ClassTypeUtils.getFirstCharLowName(clazz);
		}
		MapperXml xmlInfo=application.xmlHelper.getMapperXmlInfo(clazz);
		
		if(StringUtils.notEmpty(mapperAnn.target())) {
			xmlInfo.setTarget(mapperAnn.target());
		}
		
		if(StringUtils.notEmpty(mapperAnn.template())) {
			xmlInfo.setTemplate(mapperAnn.template());
		}
		
		if(StringUtils.notEmpty(mapperAnn.sqlType())) {
			xmlInfo.setSqlType(mapperAnn.sqlType());
		}
		
		if(mapperAnn.single()==false) {
			xmlInfo.setSingle(mapperAnn.single());
		}
		if(StringUtils.notEmpty(mapperAnn.namespace())) {
			xmlInfo.setNamespace(mapperAnn.namespace());
		}
		if(StringUtils.notEmpty(mapperAnn.include())) {
			xmlInfo.setInclude(mapperAnn.include());
		}
		DaoMapper mapper=new DaoMapper(application,xmlInfo);
		mapper.setName(daoName);
		mapper.setDaoClass(clazz);
		mapper.doInit();
		return mapper;
	}
}
