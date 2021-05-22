package org.smile.io;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.Map;

import org.smile.collection.BaseMapEntry;

public class ResourceUtils{
	/** 用于标记是class目录下的文件 */
	public static final String CLASSPATH_PREFIX = "classpath:";
	/***/
	public static final String URL_PREFIX = "url:";
	/** 用于标记是一个文件 */
	public static final String FILE_PREFIX = "file:";

	public static boolean hasResourcePrefix(String resourcePath) {
		return (resourcePath != null) && ((resourcePath.startsWith(CLASSPATH_PREFIX)) || (resourcePath.startsWith(URL_PREFIX)) || (resourcePath.startsWith(FILE_PREFIX)));
	}

	public static boolean resourceExists(String resourcePath) {
		InputStream stream = null;
		boolean exists = false;
		try {
			stream = getInputStreamForPath(resourcePath);
			exists = true;
		} catch (IOException e) {
			stream = null;
		} finally {
			IOUtils.close(stream);
		}
		return exists;
	}

	/**
	 * 从一个配置的路径打开文件 可以是classpath: 开头
	 * url:     file:       开头
	 * @param resourcePath
	 * @return
	 * @throws IOException
	 */
	public static InputStream getInputStreamForPath(String resourcePath) throws IOException {
		InputStream is;
		if (resourcePath.startsWith(CLASSPATH_PREFIX)) {
			is = loadFromClassPath(stripPrefix(resourcePath));
		} else {
			if (resourcePath.startsWith(URL_PREFIX)) {
				is = loadFromUrl(stripPrefix(resourcePath));
			} else {
				if (resourcePath.startsWith(FILE_PREFIX)) {
					is = loadFromFile(stripPrefix(resourcePath));
				} else{
					is = loadFromFile(resourcePath);
				}
			}
		}
		if (is == null) {
			throw new IOException("Resource [" + resourcePath + "] could not be found.");
		}

		return is;
	}

	public static InputStream loadFromFile(String path) throws IOException {
		return new FileInputStream(path);
	}

	public static InputStream loadFromUrl(String urlPath) throws IOException {
		URL url = new URL(urlPath);
		return url.openStream();
	}

	public static InputStream loadFromClassPath(String path) {
		return Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
	}

	private static String stripPrefix(String resourcePath) {
		return resourcePath.substring(resourcePath.indexOf(":") + 1);
	}

	public static InputStream getResourceAsStream(String configFileName) {
		return loadFromClassPath(configFileName);
	}
	/**
	 * 以一个文件名尝试多种扩展名
	 * @param fileName
	 * @param extensions
	 * @return
	 */
	public static Map.Entry<String, InputStream> getResourceAsStream(String fileName,Collection<String> extensions) {
		for(String ext:extensions) {
			String filename=FileNameUtils.concatNameAndExtension(fileName, ext);
			InputStream is=loadFromClassPath(filename);
			if(is!=null) {
				return new BaseMapEntry<String,InputStream>(ext, is);
			}
		}
		return null;
	}
}
