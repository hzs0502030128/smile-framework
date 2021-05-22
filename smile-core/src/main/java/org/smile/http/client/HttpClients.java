package org.smile.http.client;

import java.io.IOException;
import java.nio.ByteBuffer;

public class HttpClients {
	
	private HttpClients(){}
	
	private static DefaultHttpClient client= new DefaultHttpClient();
	
	/**
	 * 默认的客户端
	 * @return
	 */
	public static HttpClient defaultClient(){
		return client;
	}
	/**
	 * 使用默认客户端的执行http请求
	 * @param req
	 * @return
	 * @throws IOException
	 */
	public static DefaultHttpResponse execute(HttpRequest<?> req) throws IOException{
		HttpResponse<ByteBuffer> response= client.execute(req);
		return (DefaultHttpResponse) response;
	}
	
	/**
	 * 指定response接收返回信息
	 * @param req 请求
	 * @param newResponse 指定的接收reponse实例
	 * @return
	 * @throws IOException
	 */
	public static <C> HttpResponse<C> execute(HttpRequest<?> req,AbstractHttpResponse<C> newResponse) throws IOException{
		HttpResponse<C> response= client.execute(req,newResponse);
		return response;
	}
}
