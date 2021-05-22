package org.smile.orm.load;

import java.io.InputStream;

import org.smile.commons.XmlParser;
import org.smile.config.BeanConfig;
import org.smile.config.BeanCreator;

public class ApplicationLoader {
	/**
	 * 加载classpath下的配置文件
	 * @param file
	 * @return
	 */
	public static ApplicationConfig loadClassXmlConfig(String file){
		InputStream is=ApplicationLoader.class.getClassLoader().getResourceAsStream(file);
		return loadXml(is);
	}
	/**
	 * 加载配置文件 
	 * @param is
	 * @return
	 */
	public static ApplicationConfig loadXml(InputStream is){
		ApplicationConfig config=new ApplicationConfig();
		loadXml(is, config);
		return config;
	}
	
	public static void loadXml(InputStream is,ApplicationConfig toConfig) {
		XmlParser parser=new XmlParser();
		try {
			ApplicationXml xml=parser.parserXml(ApplicationXml.class, is);
			for(BeanConfig bc:xml.getBean()){
				BeanCreator creator=new BeanCreator(bc);
				String key=creator.getBeanId();
				if(key==null){
					key=ApplicationConfig.DEFAULT;
				}
				Application app=(Application)creator.getBean();
				app.initOrmApplication();
				toConfig.addApplication(key,app);
			}
		} catch (Exception e) {
			throw new RuntimeException("加载文件初始化文件出错", e);
		}
	}
}
