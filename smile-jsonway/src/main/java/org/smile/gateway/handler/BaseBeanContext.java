package org.smile.gateway.handler;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;

import org.smile.io.IOUtils;
import org.smile.log.LoggerHandler;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class BaseBeanContext implements LoggerHandler{
	
	private static String CONFIG_FILE="jsongateway_beans.xml";
	
	/**
	 * 保存文件中配置的所有的bean名字与类名对应
	 */
	public static Map<String,String> beanMap=new HashMap<String,String>();
	
	static{
		InputStream is=null;
		try{
			is=BaseBeanContext.class.getClassLoader().getResourceAsStream(CONFIG_FILE);
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
			NodeList nodeList = doc.getElementsByTagName("bean");
			int length=nodeList.getLength();
			Element element;
			for(int i=0;i<length;i++){
				element = (Element) nodeList.item(i);
				beanMap.put(element.getAttribute("name"), element.getAttribute("class"));
			}
			logger.info("josngateway配置文件加载完成:"+beanMap);
		}
		catch(Exception e)
		{
			logger.error("josngateway配置文件加载失败:"+e);
		}finally{
			IOUtils.close(is);
		}
	}
	
	public static String getBeanClassName(String str)
	{
		return (String) beanMap.get(str) ;
	}
	
	public static Object getBean(String str) throws Exception
	{
		try {
			return Class.forName(getBeanClassName(str)).newInstance();
		} catch (Exception e) {
			throw new Exception("实例化一个JSONGateWay服务出错",e);
		}
	}
}
