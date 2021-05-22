package org.smile.config.parser;

import java.util.List;
import java.util.Map;

import org.smile.beans.BeanProperties;
import org.smile.beans.converter.BeanException;
import org.smile.config.BeanConfig;
import org.smile.config.EntryConfig;
import org.smile.config.ListConfig;
import org.smile.config.MapConfig;
import org.smile.config.PropertiesConfig;
import org.smile.config.PropertyConfig;
import org.smile.config.ValueConfig;
import org.smile.util.Properties;
/**
 * 用于对xml配置标签的解析接口
 * @author 胡真山
 *
 */
public interface TagParser {

	/**
	 * 解析一个属性配置
	 * @param pc
	 * @return
	 * @throws BeanException
	 */
	public abstract Object parse(PropertyConfig pc) throws BeanException;

	/**
	 * 解析一个bean配置
	 * @param bc
	 * @return
	 * @throws BeanException
	 */
	public abstract Object parse(BeanConfig bc) throws BeanException;

	/**
	 * 解析一个bean配置
	 * @param bc
	 * @return
	 * @throws BeanException
	 */
	public abstract Object parse(BeanConfig bc, BeanProperties beanProperties) throws BeanException;

	/**
	 * 解析一个map 配置
	 * @param mc
	 * @return
	 * @throws BeanException
	 */
	public abstract Map<String, Object> parse(MapConfig mc) throws BeanException;

	/**
	 * 解析一个value配置
	 * @param vc
	 * @return
	 * @throws BeanException
	 */
	public abstract Object parse(ValueConfig vc) throws BeanException;

	/**
	 * 解析一个list配置
	 * @param lc
	 * @return
	 * @throws BeanException
	 */
	public abstract List<Object> parse(ListConfig lc) throws BeanException;

	/**
	 * 解析properties配置
	 * 
	 * @param pc   
	 * @return
	 * @throws BeanException
	 */
	public abstract Properties parse(PropertiesConfig pc) throws BeanException;

	/**
	 * 解析map entry 标签
	 * @param ec
	 * @return
	 * @throws BeanException
	 */
	public abstract Object parse(EntryConfig ec) throws BeanException;

}