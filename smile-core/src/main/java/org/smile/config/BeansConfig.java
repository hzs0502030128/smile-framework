package org.smile.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.smile.commons.XmlParser;
import org.smile.util.XmlUtils;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {})
@XmlRootElement(name = "beans")
public class BeansConfig {
	/**bean 配置*/
	protected List<BeanConfig> bean;
	/**包含文件*/
	protected List<IncludeConfig> include;

	public List<BeanConfig> getBean() {
		return bean;
	}
	
	
	public List<IncludeConfig> getInclude() {
		return include;
	}

	public void store(OutputStream os) throws JAXBException{
		XmlParser parser=new XmlParser();
		parser.store(this, os);
	}
	/**
	 * 从输入流中解析对象
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public static BeansConfig parserXml(InputStream is) throws IOException {
		BeansConfig config=XmlUtils.parserXml(BeansConfig.class, is);
		return config;
	}
	/**
	 * 从一个字符串解析出对象
	 * @param xml
	 * @return
	 * @throws IOException
	 */
	public static BeansConfig parserXml(String xml) throws IOException{
		return XmlUtils.parserXml(BeansConfig.class, xml);
	}
	
}
