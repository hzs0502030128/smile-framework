package org.smile.commons;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import org.smile.file.ClassPathFileScaner;
import org.smile.file.ClassPathFileScaner.BaseVisitor;
import org.smile.util.Properties;
import org.smile.util.StringUtils;

public class ServiceFinder {
	/**默认查找目录*/
	private static final String DEFAILT_FIND_DIR="META-INF/service/";
	
	/**
	 * 在默认目录里查找实现
	 * @param fileName 配置文件的文件名
	 * @param def 默认实现
	 * @return
	 */
	public static String findImpl(String fileName,String def){
		return findImpl(DEFAILT_FIND_DIR, fileName, def);
	}
	
	
	/**
	 * 查找实现类配置
	 * @param dir 查找目录
	 * @param fileName 文件名称
	 * @param def 默认实现
	 * @return
	 */
	public static String findImpl(String dir,final String fileName,String def){
		String sysPropertiesValue=System.getProperty(fileName);
		if(StringUtils.notEmpty(sysPropertiesValue)){//先出系统配置中获取内容
			return sysPropertiesValue;
		}
		final LinkedList<Properties> properties=new LinkedList<Properties>();
		ClassPathFileScaner scaner=new ClassPathFileScaner(dir,new BaseVisitor(){
			@Override
			public boolean accept(String name, String protocol) {
				return fileName.endsWith(name);
			}
			@Override
			public boolean visit(String fileName,InputStream is) throws IOException {
				Properties p=new Properties();
				p.load(is);
				if(p.isEmpty()) {
					return false;
				}
				properties.add(p);
				return true;
			}});
		try {
			scaner.scanning();
		} catch (IOException e) {
			throw new SmileRunException(e);
		}
		if(properties.size()>0){
			Properties p=properties.getFirst();
			if(p.size()>0){
				return (String)p.getKeys().iterator().next();
			}
		}
		return def;
	}
	/**
	 * 搜索配置文件
	 * @param dir
	 * @param fileName
	 * @return
	 */
	public static List<Properties> findProperties(String dir,final String fileName){
		final List<Properties> properties=new LinkedList<Properties>();
		ClassPathFileScaner scaner=new ClassPathFileScaner(dir,new BaseVisitor(){
			@Override
			public boolean accept(String name, String protocol) {
				return fileName.equals(name);
			}
			@Override
			public boolean visit(String fileName,InputStream is) throws IOException {
				Properties p=new Properties();
				p.load(is);
				properties.add(p);
				return false;
			}});
		try {
			scaner.scanning();
		} catch (IOException e) {
			throw new SmileRunException(e);
		}
		return properties;
	}
	
}
