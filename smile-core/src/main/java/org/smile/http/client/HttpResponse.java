package org.smile.http.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.smile.json.JSONAware;
import org.smile.json.parser.JSONParseException;


public interface HttpResponse<C> extends HttpBase<HttpResponse<C>>{
	/**
	 * 读取返回内容为一个字符串
	 * @return 字符串内容
	 * @throws UnsupportedEncodingException
	 */
	public String readBody() throws UnsupportedEncodingException;
	/***
	 * 读取返回内容为一个字符串
	 * @return
	 * @throws JSONParseException
	 */
	public <T extends JSONAware> T readJSON() throws JSONParseException;
	/***
	 * 获取返回内容
	 * @return
	 */
	public C getContent();
	/**
	 * 接收内容
	 * @param is
	 * @throws IOException 
	 */
	public void recieveContent(InputStream is,int maxBodySize) throws IOException;
	/**获取返回的状态码*/
	public int getStatus();

}
