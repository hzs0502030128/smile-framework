package org.smile.file;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.smile.Smile;
import org.smile.commons.SmileRunException;
import org.smile.io.FileNameUtils;
import org.smile.io.FileUtils;
import org.smile.util.URLUtils;

public class ClassPathFileScaner {
	/**根目录*/
	private String rootDir;
	/**文件名编码*/
	private String encode=Smile.ENCODE;
	/**访问者对扫描到的文件进行处理*/
	private Visitor visitor;
	
	public ClassPathFileScaner(String rootDir,Visitor visitor){
		this.visitor=visitor;
		this.rootDir=rootDir;
	}
	
	
	public void setRootDir(String rootDir) {
		this.rootDir = rootDir;
	}


	public void setEncode(String encode) {
		this.encode = encode;
	}


	public void setVisitor(Visitor visitor) {
		this.visitor = visitor;
	}


	/**
	 * 加载命令配置文件
	 * @throws IOException
	 */
	public void scanning() throws IOException {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		// 定义一个枚举的集合 并进行循环来处理这个目录下的things
		Enumeration<URL> dirs;
		try {
			dirs = classLoader.getResources(rootDir);
		} catch (IOException e) {
			throw new SmileRunException("resource dir " + rootDir, e);
		}
		// 循环迭代下去
		while (dirs.hasMoreElements()) {
			// 获取下一个元素
			URL url = dirs.nextElement();
			// 得到协议的名称
			String protocol = url.getProtocol();
			// 如果是以文件的形式保存在服务器上
			if (URLUtils.URL_PROTOCOL_FILE.equals(protocol)) {
				// 获取包的物理路径
				String filePath;
				try {
					filePath = URLDecoder.decode(url.getFile(),encode);
				} catch (UnsupportedEncodingException e) {
					throw new SmileRunException(e);
				}
				// 以文件的方式扫描整个包下的文件 并添加到集合中
				if(scanningDir(new File(filePath))){
					return ;
				}
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
				// 从此jar包 得到一个枚举类
				Enumeration<JarEntry> entries = jar.entries();
				// 同样的进行循环迭代
				while (entries.hasMoreElements()) {
					// 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
					JarEntry entry = entries.nextElement();
					if(entry.isDirectory()) {
						continue;
					}
					String name = entry.getName();
					// 如果是以/开头的
					if (name.charAt(0) == '/') {
						// 获取后面的字符串
						name = name.substring(1);
					}
					// 如果前半部分和定义的包名相同
					if (name.startsWith(rootDir)) {
						String fileName=name.substring(rootDir.length());
						if(visitor.accept(fileName, URLUtils.URL_PROTOCOL_JAR)) {
							InputStream is = jar.getInputStream(entry);
							if(visitor.visit(fileName,is)){
								return;
							}
						}
					}
				}
			}
		}
	}
	/**
	 * 扫描一个目录中的文件
	 * @param divPath
	 * @throws IOException
	 */
	private boolean scanningDir(File divPath) throws IOException{
		Set<File> files=new HashSet<File>();
		FileUtils.recursiveFiles(divPath, new FileFilter() {
			@Override
			public boolean accept(File f) {
				if(f.isDirectory()){
					return true;
				}
				return visitor.accept(f);
			}
		},files);
		for(File f:files){
			boolean r=visitor.visit(f);
			if(r){
				return true;
			}
		}
		return false;
	}
	
	public interface Visitor{
		/**
		 * 是否接受一个文件
		 * @param file
		 * @return
		 */
		boolean accept(File file);
		/***
		 * 访问一个文件
		 * @param f
		 */
		boolean  visit(File f) throws IOException;
		/***
		 * 是否接受文件名
		 * @param fileName
		 * @param protocol TODO
		 * @return
		 */
		boolean accept(String fileName, String protocol);
		/**
		 * 访问一个输入流
		 * @return if need stop scanning return true
		 * @param is
		 */
		boolean visit(String fileName,InputStream is) throws IOException;
	}
	
	public static abstract class BaseVisitor implements Visitor{
		@Override
		public boolean visit(File f) throws IOException {
			return visit(f.getName(),new FileInputStream(f));
		}

		@Override
		public boolean accept(File file) {
			String fileName=FileNameUtils.getName(file.getName());
			return accept(fileName, URLUtils.URL_PROTOCOL_FILE);
		}
	}
}
