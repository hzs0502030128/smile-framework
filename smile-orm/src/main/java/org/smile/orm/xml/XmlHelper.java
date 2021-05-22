package org.smile.orm.xml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;

import org.smile.Smile;
import org.smile.commons.XmlParser;
import org.smile.io.BufferedReader;
import org.smile.io.IOUtils;
import org.smile.log.LoggerHandler;
import org.smile.orm.xml.execut.MapperXml;
import org.smile.util.ClassPathUtils;
/**
 * Dao的xml配置加载类
 * @author 胡真山
 * @Date 2016年1月8日
 */
public class XmlHelper implements LoggerHandler{
	/**映射配置文件后缀名*/
	protected static final String FILE_EXT=".xml";
	
	private XmlParser xmlParser=new XmlParser();
	/**
	 * dao 接口对应的xml文件
	 * */
	public InputStream getDaoXml(Class<?> clazz){
		String xmlPath=getDaoXmlFileName(clazz);
		InputStream is=clazz.getClassLoader().getResourceAsStream(xmlPath);
		return is;
	}
	
	private String getDaoXmlFileName(Class<?> clazz){
		String clazzname=clazz.getSimpleName();
		String xmlPath=ClassPathUtils.getPackageDir(clazz)+clazzname+FILE_EXT;
		return xmlPath;
	}
	/**
	 * 获取xml的文件
	 * @param clazz
	 * @return
	 */
	public File getDaoXmlFile(Class<?> clazz){
		String xmlPath=getDaoXmlFileName(clazz);
		URL url=clazz.getClassLoader().getResource(xmlPath);
		if(url==null){
			return null;
		}
		return new File(url.getFile());
	}
	/**
	 * 获取class文件
	 * @param clazz
	 * @return
	 */
	public File getClassFile(Class<?> clazz){
		String classPath=ClassPathUtils.getClassFilePath(clazz);
		URL url=clazz.getClassLoader().getResource(classPath);
		if(url==null){
			return null;
		}
		return new File(url.getFile());
	}
	
	/***
	 * 解析一个xml文件流 ,jaxb 
	 * @param rootClass
	 * @param input
	 * @return
	 * @throws IOException
	 */
	public <E> E parserXml(Class<E> rootClass, InputStream input) throws IOException {
		Reader reader;
		BufferedReader in = null;
		try {
			in = new BufferedReader(input,Smile.ENCODE);
			String string = in.readToString();
			reader = new StringReader(string);
		} finally {
			IOUtils.close(in);
		}
		return xmlParser.parserXml(rootClass, reader);
	}
	/**
	 * 加载dao 的xml文件 
	 * @param clazz xml在class同包下的同文件名 只是扩展名不一样
	 * @param updateTime 从新加载时使用
	 * @return 
	 */
	public MapperXml getMapperXmlInfo(Class clazz){
		InputStream is=getDaoXml(clazz);
		MapperXml xml;
		if(is==null){
			xml=new MapperXml();
			logger.info(clazz+"没有配置对应的DAO xml ");
		}else{
			try {
				xml=parserXml(MapperXml.class, is);
				File file=getDaoXmlFile(clazz);
				xml.setXmlUpdateTimes(file.lastModified());
			} catch (Exception e) {
				throw new DaoXmlLoadException("parser dao xml has a error "+clazz, e);
			}
		}
		//修改时间
		File classFile=getClassFile(clazz);
		xml.setClassUpdateTimes(classFile.lastModified());
		return xml;
	}
}
