package org.smile.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Set;
/**
 * 配置文件接口
 * 加载一个文件 读取配置信息
 * @author 胡真山
 *
 */
public interface Config {
	
	/**
	 * 加载配置文件 
	 * @param file
	 * @throws IOException
	 */
	public void load(File file) throws IOException;
	/**
	 * 加载配置文件 
	 * @param is
	 * @throws IOException
	 */
	public void load(InputStream is) throws IOException;
	/**
	 * 加载配置文件 
	 * @param file
	 * @param initParam
	 * @throws IOException
	 */
	public void load(File file,Object initParam) throws IOException;
	/**
	 * 加载配置文件 
	 * @param is
	 * @param initParam
	 * @throws IOException
	 */
	public void load(InputStream is,Object initParam) throws IOException;
	/**
	 * 获取配置文件对应的内容
	 * @param key 配置的名称
	 * @return 配置的值
	 */
	public <T> T getValue(String key);
	/**
	 * 所有配置的值
	 * @return
	 */
	public Collection getValues();
	/**
	 * 配置的所有键
	 * @return
	 */
	public Set getKeys();
	
}
