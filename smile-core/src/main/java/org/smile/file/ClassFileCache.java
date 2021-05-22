package org.smile.file;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.smile.http.HttpConstans;
import org.smile.io.IOUtils;
import org.smile.log.LoggerHandler;
/**
 * 缓存class path中的文件 
 * @author 胡真山
 *
 */
public class ClassFileCache implements LoggerHandler,HttpConstans{
	/**文件内容缓存*/
	protected Map<String,byte[]> cacheFiles=new ConcurrentHashMap<String,byte[]>();
	/**文件修改时间*/
	protected Map<String,Long> cacheTimes=new ConcurrentHashMap<String, Long>();
	/**
	 * 获取文件内容
	 * @param pathName
	 * @return
	 */
	public byte[] findFileByPathName(String pathName){
		byte[] file=cacheFiles.get(pathName);
		if(file==null){
			synchronized (cacheFiles) {
				file=cacheFiles.get(pathName);
				try {
					InputStream is=ClassFileCache.class.getClassLoader().getResourceAsStream(pathName);
					file=IOUtils.stream2byte(is);
					cacheFiles.put(pathName, file);
					cacheTimes.put(pathName, System.currentTimeMillis());
				} catch (IOException e) {
					logger.error("读取文件 "+pathName+"失败",e);
				}
			}
		}
		return file;
	}
}
