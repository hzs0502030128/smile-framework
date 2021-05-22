package org.smile.config.parser;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.smile.beans.BeanFactorySupport;
import org.smile.beans.BeanProperties;
import org.smile.beans.converter.BeanException;
import org.smile.collection.CollectionUtils;
import org.smile.config.BeanConfig;
import org.smile.config.BeanCreateException;
import org.smile.config.EntryConfig;
import org.smile.config.ListConfig;
import org.smile.config.MapConfig;
import org.smile.config.PropertiesConfig;
import org.smile.config.PropertyConfig;
import org.smile.config.ValueConfig;
import org.smile.reflect.ClassTypeUtils;
import org.smile.reflect.MethodUtils;
import org.smile.util.Properties;
import org.smile.util.StringUtils;
/**
 * 配置标签解析
 * @author 胡真山
 *
 */
public class TagConfigParser implements TagParser {
	/**注册到的工厂*/
	private BeanFactorySupport factory;
	
	public TagConfigParser(BeanFactorySupport factory){
		this.factory=factory;
	}
	
	public TagConfigParser(){}

	/**
	 * 设置参考的属性值 
	 * @param bean
	 * @throws BeanException
	 */
	protected void setRef(BeanConfig config,Object bean,BeanProperties beanProperties) throws BeanException{
		//配置的属性
		List<PropertyConfig> properties=config.getProperty();
		setPropertyRef(properties, bean,beanProperties);
		setListRef(config.getList(), bean,beanProperties);
		setMapRef(config.getMap(), bean,beanProperties);
	}
	
	/**
	 * 	设置属性参考
	 * @param properties
	 * @param bean
	 * @param beanProperties
	 * @throws BeanException
	 */
	protected void setPropertyRef(List<PropertyConfig> properties,Object bean,BeanProperties beanProperties) throws BeanException{
		if(CollectionUtils.notEmpty(properties)){
			for(PropertyConfig pc:properties){
				String name=pc.getName();
				String ref=pc.getRef();
				if(ref!=null){
					Object refBean=factory.getBean(ref);
					beanProperties.setFieldValue(name, refBean, bean);
				}
			}
		}
	}
	/**
	 * 设置list属性的参考
	 * @param list
	 * @param bean
	 * @throws BeanException
	 */
	protected void setListRef(List<ListConfig> list,Object bean,BeanProperties beanProperties) throws BeanException{
		if(CollectionUtils.notEmpty(list)){
			for(ListConfig lc:list){
				String name=lc.getName();
				List<ValueConfig> values=lc.getValue();
				if(CollectionUtils.notEmpty(values)){
					List<String> valueList=ListConfig.convertRefToList(values);
					if(CollectionUtils.notEmpty(valueList)){
						List<Object> refList=new LinkedList<Object>();
						for(String str:valueList){
							refList.add(factory.getBean(str));
						}
						beanProperties.setFieldValue(name, refList, bean);
					}
				}
			}
		}
	}
	/**
	 * 设置map属性的参考
	 * @param maps
	 * @param bean
	 * @throws BeanException
	 */
	protected void setMapRef(List<MapConfig> maps,Object bean,BeanProperties beanProperties) throws BeanException{
		if(CollectionUtils.notEmpty(maps)){
			for(MapConfig lc:maps){
				String name=lc.getName();
				List<PropertyConfig> pc=lc.getProperty();
				if(CollectionUtils.notEmpty(pc)){
					Map<String,String> map=MapConfig.convertRefToMap(pc);
					if(CollectionUtils.notEmpty(map)){
						Map<String,Object> refMap=new HashMap<String, Object>();
						for(Map.Entry<String, String> entry:map.entrySet()){
							refMap.put(entry.getKey(), factory.getBean(entry.getValue()));
						}
						beanProperties.setFieldValue(name, refMap, bean);
					}
				}
			}
		}
	}
	@Override
	public Object parse(PropertyConfig pc) throws BeanException{
		if(StringUtils.notEmpty(pc.getValue())){
			return pc.getValue();
		}else if(pc.getBeanConfig()!=null){
			if(pc.getName()==null){
				pc.setName(pc.getBeanConfig().getId());
			}
			return parse(pc.getBeanConfig());
		}else if(pc.getList()!=null){
			if(pc.getName()==null){
				pc.setName(pc.getList().getName());
			}
			return parse(pc.getList());
		}else if(pc.getMap()!=null){
			return parse(pc.getMap());
		}else if(pc.getValueConfig()!=null){
			return parse(pc.getValueConfig());
		}else if(pc.getProperties()!=null){
			if(pc.getName()==null){
				pc.setName(pc.getProperties().getName());
			}
			return parse(pc.getProperties());
		}else if(pc.getRef()!=null){
			
		}
		return null;
	}
	@Override
	public Object parse(BeanConfig bc) throws BeanException{
		try{
			BeanProperties beanProperties=new BeanProperties();
			return parse(bc, beanProperties);
		}catch(Throwable e){
			throw new BeanCreateException("实例化bean出错:"+bc.getClazz(), e);
		}
	}
	
	/**
	 * 调用初始化方法
	 * @param bean
	 * @throws BeanException
	 */
	protected void invokeInitializingMethod(Class clazz,BeanConfig bc,Object bean) throws BeanException{
		if(bc.getInitializing()!=null){
			Method initializingMethod=MethodUtils.getMethod(clazz, bc.getInitializing());
			try{
				initializingMethod.invoke(bean);
			}catch(Exception e){
				throw new BeanException(bc.getInitializing(),e);
			}
		}
	}
	
	@Override
	public Object parse(BeanConfig bc,BeanProperties beanProperties) throws BeanException{
		Object bean=ClassTypeUtils.newInstance(bc.getClazz());
		List<PropertyConfig> properties=bc.getProperty();
		setBeanPropertyValue(beanProperties, properties, bean);
		//赋值list配置
		List<ListConfig> list=bc.getList();
		setBeanListValue(beanProperties, list, bean);
		//赋值map配置
		List<MapConfig> mapList=bc.getMap();
		setBeanMapValue(beanProperties, mapList, bean);
		if(factory!=null){//存在工厂时才可以支持参考属性
			setRef(bc, bean, beanProperties);
		}
		invokeInitializingMethod(bean.getClass(), bc, bean);
		return bean;
		
	}
	
	protected void setBeanPropertyValue(BeanProperties beanProperties,List<PropertyConfig> properties,Object bean) throws BeanException{
		if(CollectionUtils.notEmpty(properties)){
			for(PropertyConfig pc:properties){
				Object propertyValue=parse(pc);
				beanProperties.setFieldValue(pc.getName(),propertyValue, bean);
			}
		}
	}
	
	protected void setBeanListValue(BeanProperties beanProperties,List<ListConfig> list,Object bean) throws BeanException{
		if(CollectionUtils.notEmpty(list)){
			for(ListConfig lc:list){
				List<Object> listValues=parse(lc);
				beanProperties.setFieldValue(lc.getName(), listValues, bean);
			}
		}
	}
	
	protected void setBeanMapValue(BeanProperties beanProperties,List<MapConfig> mapList,Object bean) throws BeanException{
		if(CollectionUtils.notEmpty(mapList)){
			for(MapConfig mc:mapList){
				Map<String,Object> mapValue=parse(mc);
				beanProperties.setFieldValue(mc.getName(), mapValue, bean);
			}
		}
	}
	
	@Override
	public Map<String,Object> parse(MapConfig mc) throws BeanException{
		Map<String,Object> mapValue=new LinkedHashMap<String, Object>();
		List<PropertyConfig> pcList=mc.getProperty();
		if(CollectionUtils.notEmpty(pcList)){
			for(PropertyConfig pc:pcList){
				mapValue.put(pc.getName(),parse(pc));
			}
		}
		//
		List<EntryConfig> entryList=mc.getEntry();
		if(CollectionUtils.notEmpty(entryList)){
			for(EntryConfig pc:entryList){
				mapValue.put(pc.getKey(),parse(pc));
			}
		}
		return mapValue;
	}
	
	@Override
	public Object parse(ValueConfig vc) throws BeanException{
		String value=vc.getValue();
		if(StringUtils.notEmpty(value)){
			return value;
		}else if(vc.getBeanConfig()!=null){
			Object obj=parse(vc.getBeanConfig());
			return obj;
		}
		return null;
	}
	@Override
	public List<Object> parse(ListConfig lc) throws BeanException{
		List<Object> listValues=null;
		//解析value标签
		List<ValueConfig> values=lc.getValue();
		if(CollectionUtils.notEmpty(values)){
			listValues=new ArrayList<Object>(values.size());
			for(ValueConfig vc:values){
				listValues.add(parse(vc));
			}
		}
		//解析bean标签
		List<BeanConfig> beans=lc.getBean();
		if(CollectionUtils.notEmpty(beans)){
			if(listValues==null){//为空时创建list
				listValues=new ArrayList<Object>(beans.size());
			}
			for(BeanConfig vc:beans){
				listValues.add(parse(vc));
			}
		}
		return listValues;
	}
	@Override
	public Properties parse(PropertiesConfig pc) throws BeanException{
		return pc.getProperties();
	}
	
	@Override
	public Object parse(EntryConfig ec) throws BeanException{
		if(StringUtils.notEmpty(ec.getValue())){
			return ec.getValue();
		}
		if(ec.getValueConfig()!=null){
			return parse(ec.getValueConfig());
		}
		return null;
	}
}
