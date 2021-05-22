package org.smile.util;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.jar.JarFile;

import org.smile.Smile;
import org.smile.commons.ExceptionUtils;
import org.smile.commons.SmileRunException;
import org.smile.commons.Strings;
import org.smile.log.LoggerHandler;


public class ClassPathUtils implements LoggerHandler{
	
	private static final RegExp ClASS_NAME_SPLIT=new RegExp("\\.");
	/**class文件扩展名*/
	private static final String CLASS_EXT=".class";
	
	/**
	 * 获取类的包路径
	 * @param clazz
	 * @return
	 */
	public static String getPackageDir(Class clazz){
		String name=clazz.getName();
		String simplename=clazz.getSimpleName();
		return ClASS_NAME_SPLIT.replaceAll(name,"\\/").replace(simplename, Strings.BLANK);
	}
	
	public static String getPackageDir(String packages) {
		return ClASS_NAME_SPLIT.replaceAll(packages,"\\/")+"/";
	}
	/**
	 * 获取类文件名路
	 * @param clazz
	 * @return
	 */
	public static String getClassFilePath(String classPath,Class clazz){
		String name=clazz.getName();
		return classPath+ClASS_NAME_SPLIT.replaceAll(name, "\\/")+CLASS_EXT;
	}
	
	public static String getClassFilePath(Class clazz){
		String name=clazz.getName();
		return ClASS_NAME_SPLIT.replaceAll(name, "\\/")+CLASS_EXT;
	}
	/**
	 * 获取classpath目录 
	 * @return
	 */
	public static List<File> getClassPathDir(){
		ClassLoader classLoader=Thread.currentThread().getContextClassLoader();
		Enumeration<URL> dirs;
		List<File> files=new LinkedList<File>();
		try {
			dirs = classLoader.getResources(Strings.BLANK);
			// 循环迭代下去
			while (dirs.hasMoreElements()) {
				// 获取下一个元素
				URL url = dirs.nextElement();
				// 得到协议的名称
				String protocol = url.getProtocol();
				// 如果是以文件的形式保存在服务器上
				if (URLUtils.URL_PROTOCOL_FILE.equals(protocol)) {
					// 获取包的物理路径
					String filePath = URLDecoder.decode(url.getFile(),Smile.ENCODE);
					File dir=new File(filePath);
					if(dir.isDirectory()){
						files.add(dir);
					}
				}
			}
		}catch(Exception e){
			logger.error(ExceptionUtils.getExceptionMsg(e));
		}
		return files;
	}
	
	/**
	 * 获取classpath目录 
	 * @return
	 */
	public static Set<String> getClassPathDirAndJar(){
		ClassLoader classLoader=Thread.currentThread().getContextClassLoader();
		Enumeration<URL> dirs;
		Set<String> files=new LinkedHashSet<String>();
		try {
			dirs = classLoader.getResources(Strings.BLANK);
			// 循环迭代下去
			while (dirs.hasMoreElements()) {
				// 获取下一个元素
				URL url = dirs.nextElement();
				// 得到协议的名称
				String protocol = url.getProtocol();
				// 如果是以文件的形式保存在服务器上
				if (URLUtils.URL_PROTOCOL_FILE.equals(protocol)) {
					// 获取包的物理路径
					String filePath = URLDecoder.decode(url.getFile(),Smile.ENCODE);
					File dir=new File(filePath);
					if(dir.isDirectory()){
						files.add(dir.getPath());
					}
				}else if (URLUtils.URL_PROTOCOL_JAR.equals(protocol)) {
					JarFile jar;
					// 获取jar
					try {
						jar = ((JarURLConnection) url.openConnection()).getJarFile();
						files.add(jar.getName());
					} catch (IOException e) {
						throw new SmileRunException(e);
					}
				}
			}
		}catch(Exception e){
			logger.error(ExceptionUtils.getExceptionMsg(e));
		}
		return files;
	}
	
	
	
	public static Set<String> getClassPathJar(){
		ClassLoader classLoader=Thread.currentThread().getContextClassLoader();
		Enumeration<URL> dirs;
		Set<String> files=new LinkedHashSet<String>();
		try {
			dirs = classLoader.getResources(Strings.BLANK);
			// 循环迭代下去
			while (dirs.hasMoreElements()) {
				// 获取下一个元素
				URL url = dirs.nextElement();
				// 得到协议的名称
				String protocol = url.getProtocol();
				// 如果是以文件的形式保存在服务器上
				if (URLUtils.URL_PROTOCOL_JAR.equals(protocol)) {
					JarFile jar;
					// 获取jar
					try {
						jar = ((JarURLConnection) url.openConnection()).getJarFile();
						files.add(jar.getName());
					} catch (IOException e) {
						throw new SmileRunException(e);
					}
				}
			}
		}catch(Exception e){
			logger.error(ExceptionUtils.getExceptionMsg(e));
		}
		return files;
	}
}
