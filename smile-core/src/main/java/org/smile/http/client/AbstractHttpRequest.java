package org.smile.http.client;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.smile.Smile;
import org.smile.beans.converter.ConvertException;
import org.smile.commons.SmileRunException;
import org.smile.commons.Strings;
import org.smile.http.HttpConstans;
import org.smile.http.HttpMethod;
import org.smile.util.Base64;


public abstract class AbstractHttpRequest<T extends HttpRequest<?>> extends AbstractHttpBase<T> implements HttpRequest<T>{
	 /**是否需要多媒体提交*/
	 private boolean needsMulti=false;
	 /**代理上网*/
	 private Proxy proxy; // nullable
	 /**连接超时毫秒数*/
	 protected int timeoutMilliseconds;
	 /**最在提交数据*/
	 protected int maxBodySizeBytes;
	 /**接受重定向*/
	 protected boolean followRedirects;
	 /**请求体内容字符串*/
	 protected String body = null;
	 /**忽略响应的错误*/
	 protected boolean ignoreHttpErrors = false;
	 /**忽略响应的contentType*/
	 protected boolean ignoreContentType = false;
	 /**是否需要验证证书链*/
	 protected boolean validateTSLCertificates = false;
	 /**封装请求数据*/
	 private List<RequestValue> datas=new ArrayList<RequestValue>();
	 /**POST提交数据时的编码*/
	 protected String postDataCharset;;

     protected AbstractHttpRequest() {
		timeoutMilliseconds = Smile.config.getValue(Smile.HTTP_TIMEOUT_KEY, Integer.class, 5000);
         maxBodySizeBytes = 2*1024 * 1024; // 2MB
         followRedirects = true;
         method = HttpMethod.GET;
         headers.put("Accept-Encoding", "gzip");
     }

     @Override
     public Proxy proxy() {
         return proxy;
     }
     @Override
     public T proxy(Proxy proxy) {
         this.proxy = proxy;
         return (T)this;
     }
     @Override
     public T proxy(String host, int port) {
         this.proxy = new Proxy(Proxy.Type.HTTP, InetSocketAddress.createUnresolved(host, port));
         return (T)this;
     }
     @Override
     public int timeout() {
         return timeoutMilliseconds;
     }
     @Override
     public T timeout(int millis) {
         timeoutMilliseconds = millis;
         return (T)this;
     }
     @Override
     public int maxBodySize() {
         return maxBodySizeBytes;
     }
     @Override
     public T maxBodySize(int bytes) {
    	 if(bytes>0){
    		 maxBodySizeBytes = bytes;
             return (T)this;
    	 }
         throw new SmileRunException("maxSize must be 0 (unlimited) or larger");
        
     }
     @Override
     public boolean followRedirects() {
         return followRedirects;
     }
     @Override
     public T followRedirects(boolean followRedirects) {
         this.followRedirects = followRedirects;
         return (T)this;
     }
     @Override
     public boolean ignoreHttpErrors() {
         return ignoreHttpErrors;
     }
     @Override
     public boolean validateTLSCertificates() {
         return validateTSLCertificates;
     }
     @Override
     public void validateTLSCertificates(boolean value) {
         validateTSLCertificates = value;
     }
     @Override
     public T ignoreHttpErrors(boolean ignoreHttpErrors) {
         this.ignoreHttpErrors = ignoreHttpErrors;
         return (T)this;
     }
     @Override
     public boolean ignoreContentType() {
         return ignoreContentType;
     }
     @Override
     public T ignoreContentType(boolean ignoreContentType) {
         this.ignoreContentType = ignoreContentType;
         return (T)this;
     }
     @Override
     public T data(RequestValue reqVal) {
         datas.add(reqVal);
         if(reqVal.isStream()){
        	 this.needsMulti=true;
         }
         return (T)this;
     }
     @Override
     public T requestBody(String body) {
         this.body = body;
         return (T)this;
     }
     @Override
     public T requestJsonBody(String body) {
         this.body = body;
         this.contentType=HttpConstans.JSON_CONTENT_TYPE;
         return (T)this;
     }
     
     @Override
     public String requestBody() {
         return body;
     }
     
     @Override
     public T postDataCharset(String charset) {
         if (!Charset.isSupported(charset)) throw new IllegalCharsetNameException(charset);
         this.postDataCharset = charset;
         return (T)this;
     }
     
     @Override
     public String postDataCharset() {
         return postDataCharset==null?charset:postDataCharset;
     }
     
     @Override
 	public Collection<RequestValue> datas() {
 		return this.datas;
 	}


	@Override
	public boolean hasProxy() {
		return proxy!=null;
	}

	@Override
	public T data(String name, String value) {
		return data(new KeyValue(name, value));
	}
	
	@Override
	public boolean needsMultipart() {
		return this.needsMulti;
    }

	@Override
	public boolean hasRequestBody() {
		return this.body!=null;
	}

	@Override
	public T data(Map<String, Object> datas) {
		for(Map.Entry<String, Object> entry:datas.entrySet()){
			data(entry.getKey(), String.valueOf(entry.getValue()));
		}
		return (T)this;
	}
	
	/**
	 * 上传文件
	 * @param name
	 * @param file
	 */
	public T file(String name,File file){
		InputStream is;
		try {
			is = new BufferedInputStream(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			throw new SmileRunException(e);
		}
		return this.data(new StreamValue(name, file.getName(),is));
	}
	/**
	 * 上传一个流
	 * @param name
	 * @param fileName
	 * @param is
	 */
	public T stream(String name,String fileName,InputStream is){
		return this.data(new StreamValue(name, fileName,is));
	}
	/**
	 * 	基本认证信息
	 * @param username
	 * @param password
	 * @return
	 */
	public T authorization(String username,String password) {
		String author=username;
		if(password!=null) {
			author+=":"+password;
		}
		String encodeStr=null;
		try {
			encodeStr = Base64.encode(author.getBytes(Strings.UTF_8));
		} catch (UnsupportedEncodingException e) {
			encodeStr=Base64.encode(author.getBytes());
		}
		return this.header(HttpConstans.AUTHOR, HttpConstans.BASIC_AUTHOR_PREFIX+encodeStr);
	}
	
}
