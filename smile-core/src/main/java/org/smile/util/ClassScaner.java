package org.smile.util;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.smile.Smile;
import org.smile.commons.SmileRunException;
import org.smile.commons.Strings;
import org.smile.log.LoggerHandler;

/***
 * 扫描类
 * 
 * @author 胡真山
 *
 */
public class ClassScaner implements LoggerHandler {

	private ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
	/** 包括过滤 */
	private Set<RegExp> includeFilter = new HashSet<RegExp>();

	/**
	 * 添加过滤条件 正则
	 * @param reg
	 */
	public void addIncludeFilter(String reg) {
		this.includeFilter.add(new RegExp(reg));
	}
	/**
	 * 从包package中获取所有的Class
	 * 
	 * @param pack
	 * @return
	 */
	public Set<Class<?>> getClasses(String pack) {
		// 第一个class类的集合
		Set<Class<?>> classes = new LinkedHashSet<Class<?>>();
		// 是否循环迭代
		boolean recursive = true;
		// 获取包的名字 并进行替换
		String packageName = pack;
		String packageDirName = packageName.replace('.', '/');
		// 定义一个枚举的集合 并进行循环来处理这个目录下的things
		Enumeration<URL> dirs;
		try {
			dirs = classLoader.getResources(packageDirName);
		} catch (IOException e) {
			throw new SmileRunException("resource dir " + packageDirName, e);
		}
		// 循环迭代下去
		while (dirs.hasMoreElements()) {
			// 获取下一个元素
			URL url = dirs.nextElement();
			// 得到协议的名称
			String protocol = url.getProtocol();
			// 如果是以文件的形式保存在服务器上
			if (URLUtils.URL_PROTOCOL_FILE.equals(protocol)){
				// 获取包的物理路径
				String filePath;
				try {
					filePath = URLDecoder.decode(url.getFile(), Smile.ENCODE);
				} catch (UnsupportedEncodingException e) {
					throw new SmileRunException(e);
				}
				// 以文件的方式扫描整个包下的文件 并添加到集合中
				findAndAddClassesInPackageByFile(packageName, filePath, recursive, classes);
			} else if (URLUtils.URL_PROTOCOL_JAR.equals(protocol)) {
				// 如果是jar包文件
				// 定义一个JarFile
				JarFile jar;
				// 获取jar
				try {
					jar = ((JarURLConnection) url.openConnection()).getJarFile();
				} catch (IOException e) {
					throw new SmileRunException(e);
				}
				findAndAddClassesInPackageByJar(packageName, packageDirName, jar, recursive, classes);
			}
		}
		return classes;
	}
	/**
	 * 获取一个jar中的包下的文件类
	 * @param packageName 包名
	 * @param packageDirName
	 * @param jar
	 * @param recursive
	 * @param classes
	 */
	public void findAndAddClassesInPackageByJar(String packageName,String packageDirName, JarFile jar, final boolean recursive, Set<Class<?>> classes){
		// 从此jar包 得到一个枚举类
		Enumeration<JarEntry> entries = jar.entries();
		// 同样的进行循环迭代
		while (entries.hasMoreElements()) {
			// 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
			JarEntry entry = entries.nextElement();
			String name = entry.getName();
			// 如果是以/开头的
			if (name.charAt(0) == '/') {
				// 获取后面的字符串
				name = name.substring(1);
			}
			// 如果前半部分和定义的包名相同
			if (name.startsWith(packageDirName)) {
				int idx = name.lastIndexOf('/');
				// 如果以"/"结尾 是一个包
				if (idx != -1) {
					// 获取包名 把"/"替换成"."
					packageName = name.substring(0, idx).replace('/', '.');
				}
				// 如果可以迭代下去 并且是一个包
				if ((idx != -1) || recursive) {
					// 如果是一个.class文件 而且不是目录
					if (name.endsWith(Strings.DOT_CLASS) && !entry.isDirectory()) {
						// 去掉后面的".class" 获取真正的类名
						String className = name.substring(packageName.length() + 1, name.length() - 6);
						try {
							// 添加到classes集合中去
							classes.add(classLoader.loadClass(packageName + '.' + className));
						} catch (ClassNotFoundException e) {
							logger.error(e);
						}
					}
				}
			}
		}
	}

	/**
	 * 以文件的形式来获取包下的所有Class
	 * 
	 * @param packageName
	 * @param packagePath
	 * @param recursive
	 * @param classes
	 */
	public void findAndAddClassesInPackageByFile(String packageName, String packagePath, final boolean recursive, Set<Class<?>> classes) {
		// 获取此包的目录 建立一个File
		File dir = new File(packagePath);
		// 如果不存在或者 也不是目录就直接返回
		if (!dir.exists() || !dir.isDirectory()) {
			// log.warn("用户定义包名 " + packageName + " 下没有任何文件");
			return;
		}
		// 如果存在 就获取包下的所有文件 包括目录
		File[] dirfiles = dir.listFiles(new FileFilter() {
			// 自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)
			public boolean accept(File file) {
				return (recursive && file.isDirectory()) || (file.getName().endsWith(Strings.DOT_CLASS));
			}
		});
		// 循环所有文件
		for (File file : dirfiles) {
			// 如果是目录 则继续扫描
			if (file.isDirectory()) {
				findAndAddClassesInPackageByFile(packageName + "." + file.getName(), file.getAbsolutePath(), recursive, classes);
			} else {
				if (!include(file.getAbsolutePath())) {
					continue;
				}
				// 如果是java类文件 去掉后面的.class 只留下类名
				String className = file.getName().substring(0, file.getName().length() - 6);
				try {
					// 添加到集合中去
					classes.add(classLoader.loadClass(packageName + '.' + className));
				} catch (ClassNotFoundException e) {
					logger.error(e);
				}
			}
		}
	}

	/**过滤 */
	private boolean include(String name) {
		for (RegExp reg : this.includeFilter) {
			if (reg.test(name)) {
				return true;
			}
		}
		return includeFilter.size() == 0;
	}
}
