 package org.smile.reflect;

import java.io.File;
import java.io.FileFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;
import java.util.Set;

import org.smile.collection.ConcurrentHashSet;
import org.smile.commons.SmileRunException;


public class SmileClassLoader extends URLClassLoader {
	
	private Set<File> libPath=new ConcurrentHashSet<File>();

	public SmileClassLoader(){
		this(new URL[]{});
	}

	/**
	 * URL 以'/'结尾的为目录
	 *     否则为jar包
	 *     未指定其父类加载器为系统类加载器
	 * @param urls
	 */
	public SmileClassLoader(URL[] urls) {
		super(urls);
	}
	
	

	/**
	 * 同上，指定classLoader
	 * @param urls
	 * @param parent
	 */
	public SmileClassLoader(URL[] urls, ClassLoader parent) {
		super(urls,parent);
	}

	/**
	 * 同上,URL工厂处理器
	 * @param urls
	 * @param parent
	 * @param factory
	 */
	public SmileClassLoader(URL[] urls, ClassLoader parent,URLStreamHandlerFactory factory) {
		 super(urls,parent,factory);
	}

	/**
	 * 查找类对象
	 *   从以上的URLS中查找加载当前类对象[会打开所有的jars去查找指定的类]
	 *   (可以通过调用findClass来得到以上URL加载包中的类)
	 */
	public Class<?> findClass(String name) throws ClassNotFoundException {
		return super.findClass(name);
	}
	/***
	 * 
	 * @param dir
	 */
	public void addLibPath(File dir){
		this.libPath.add(dir);
		addPathUrl(dir);
	}
	/**
	 * 添加jar目录
	 * @param dir
	 */
	protected void addPathUrl(File dir){
		File[] jarList=dir.listFiles(new FileFilter(){
			@Override
			public boolean accept(File pathname){
				if(pathname.isDirectory()){
					return true;
				}
				return pathname.getName().endsWith(".jar");
			}
		});
		for(File f:jarList){
			if(f.isDirectory()){
				addPathUrl(f);
			}else{
				try {
					addURL(f.toURI().toURL());
				} catch (MalformedURLException e) {
					throw new SmileRunException("add jar file error,"+f, e);
				}
			}
		}
	}
}

