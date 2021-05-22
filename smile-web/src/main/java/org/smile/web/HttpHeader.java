package org.smile.web;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.BindingProvider;

import org.smile.commons.Strings;
import org.smile.http.HttpConstans;
import org.smile.util.Base64;
import org.smile.util.StringUtils;
import org.smile.web.author.AuthorInfo;

public class HttpHeader implements HttpConstans{
	
	private HttpServletRequest request;
	
	public HttpHeader(HttpServletRequest request){
		this.request=request;
	}
	
	public String getValue(String name){
		return request.getHeader(name);
	}
	
	public Enumeration getValues(String name){
		return request.getHeaders(name);
	}
	
	public Enumeration getNames(){
		return request.getHeaderNames();
	}
	
	public String getBasicAuthorValue(){
		String value=getValue(AUTHOR);
		if(StringUtils.isEmpty(value)){
			return null;
		}else{
			return BASIC_AUTHOR_REG.replaceAll(value, Strings.BLANK);
		}
	}
	/**
	 * 获取认证信息的用户名密码
	 * @return
	 */
	public AuthorInfo getBasicAuthorInfo(){
		String author=getBasicAuthorValue();
		if(StringUtils.isEmpty(author)){
			return null;
		}
		String string=Base64.decodeUTF(author);
		String[] info=string.split(":");
		if(info.length==1){
			return new AuthorInfo(info[0],Strings.BLANK);
		}else if(info.length==2){
			return new AuthorInfo(info[0], info[1]);
		}
		return null;
	}
	/**
	 * 响应未通过认证消息
	 * @param resp
	 */
	public void responseUnAuthorized(HttpServletResponse resp){
		resp.setStatus(STATUS_NOT_AUTHORIZED);  
		resp.setHeader("Cache-Control", "no-store");  
		resp.setDateHeader("Expires", 0);  
		resp.setHeader("WWW-authenticate", "Basic Realm=\"please input you username and password \"");  
	}
	
	public static final void putBindingProviderAuthor(BindingProvider bp,String username,String password){
		bp.getRequestContext().put(BindingProvider.USERNAME_PROPERTY,username);
		bp.getRequestContext().put(BindingProvider.PASSWORD_PROPERTY,password);
	}
	
	public Map<String,String> getHeaders(){
		Enumeration names=request.getHeaderNames();
		Map<String,String> values=new HashMap<String, String>();
		while(names.hasMoreElements()){
			String name=names.nextElement().toString();
			values.put(name, request.getHeader(name));
		}
		return values;
	}

	@Override
	public String toString() {
		return getHeaders().toString();
	}
	
}
