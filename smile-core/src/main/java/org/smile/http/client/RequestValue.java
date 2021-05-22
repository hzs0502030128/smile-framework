package org.smile.http.client;

import java.io.InputStream;
/**
 * 请求内容值
 * @author 胡真山
 *
 */
public interface RequestValue{
	/**参数名称*/
	public String name();
	/**
	 * 参数值
	 * @return
	 */
	public String value();
	/***
	 * 多媒体流数据
	 * @return
	 */
	public InputStream inputStream();
	/***
	 * 是否是流数据
	 * @return
	 */
	public boolean isStream();
}
