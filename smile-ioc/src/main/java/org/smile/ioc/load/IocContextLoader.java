package org.smile.ioc.load;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import org.smile.collection.CollectionUtils;
import org.smile.config.BeanConfig;
import org.smile.config.BeansConfig;
import org.smile.config.IncludeConfig;
import org.smile.io.ResourceUtils;
import org.smile.ioc.BeanFactory;
import org.smile.ioc.IocContext;
import org.smile.ioc.IocInitException;
import org.smile.ioc.bean.XmlBeanBuilder;
import org.smile.log.LoggerHandler;
import org.smile.util.XmlUtils;

/**
 * 加载ioc配置文件
 */
public class IocContextLoader implements LoggerHandler{
	
	/**
	 * 加载classpath下的配置文件
	 * 
	 * @param context
	 * @param file
	 */
	public  void loadClassXmlConfig(IocContext context, String file) {
		try{
			InputStream is = ResourceUtils.loadFromClassPath(file);
			List<IncludeConfig> include = loadXml(context, is);
			if (CollectionUtils.notEmpty(include)) {
				for (IncludeConfig c : include) {
					is = ResourceUtils.loadFromClassPath(c.getFile());
					loadXml(context, is);
					logger.info("load ioc config file "+c.getFile()+" success ");
				}
			}
		}catch(Exception e){
			throw new IocInitException("加载配置文件" + file + "出错", e);
		}
	}
	/**
	 * 加载IOC配置文件流
	 * @param context
	 * @param is
	 */
	public void loadClassXmlConfig(IocContext context,InputStream is) {
		List<IncludeConfig> includes;
		try {
			includes= loadXml(context, is);
		} catch (Exception e) {
			throw new IocInitException("加载配置文件出错", e);
		}
		if (CollectionUtils.notEmpty(includes)) {
			throw new IocInitException("not support include file use inputstream load");
		}
	}
	/**
	 * 加载ioc配置文件
	 * @param context
	 * @param root
	 * @param fileName
	 */
	public  void loadFileConfig(IocContext context, String root, String fileName) {
		try {
			InputStream is = new FileInputStream(fileName);
			List<IncludeConfig> includes = loadXml(context, is);
			if (CollectionUtils.notEmpty(includes)) {
				for (IncludeConfig c : includes) {
					String file=root + c.getFile();
					is = new FileInputStream(file);
					loadXml(context, is);
					logger.info("load ioc config file "+file+" success ");
				}
			}
		} catch (Exception e) {
			throw new IocInitException("加载配置文件" + fileName + "出错", e);
		}
	}

	/***
	 * 加载配置文件到容器中 如果bean配置中id为空 则会以类名做为id
	 * 
	 * @param context
	 * @param is
	 */
	public  List<IncludeConfig> loadXml(IocContext context, InputStream is) {
		try {
			BeanFactory factory = context.getBeanFactory();
			BeansConfig beans = XmlUtils.parserXml(BeansConfig.class, is);
			for (BeanConfig bc : beans.getBean()) {
				XmlBeanBuilder creator = new XmlBeanBuilder(bc);
				creator.regsitToFactory(factory);
			}
			return beans.getInclude();
		} catch (Exception e) {
			throw new IocContextLoadException("init ioc context config xml error ", e);
		}
	}
}
