package org.smile.strate.form;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.smile.beans.MapBean;
import org.smile.beans.converter.BeanException;
import org.smile.file.ContentType;
import org.smile.http.HttpConstans;
import org.smile.io.IOUtils;
import org.smile.json.JSONAware;
import org.smile.json.JSONValue;
import org.smile.reflect.MethodParamBeanClass;
import org.smile.strate.Strate;
import org.smile.strate.StrateException;
import org.smile.strate.action.Action;
import org.smile.util.RegExp;
import org.smile.util.StringUtils;
import org.smile.util.XmlUtils;

public class ActionParamBeanClass extends MethodParamBeanClass {
	/**用于拆分contentType 与 charset */
	protected static final RegExp CONTENT_TYPE_CHAREST = new RegExp(" *; *charset *= *", false);

	public ActionParamBeanClass(Method method) {
		super(method);
	}

	/**
	 * 请求体的参数名
	 */
	String requestBodyName;
	/** 请求体内容是否是非空 */
	boolean requestBodyRequried = false;
	
	String requestParamName;
	
	String responseParamName;

	/**
	 * 是否存在requestBody类型参数
	 * 
	 * @return
	 */
	public boolean hasRequestBody() {
		return requestBodyName != null;
	}

	public void requestBodyInfo(String requestBodyName, boolean requried) {
		this.requestBodyName = requestBodyName;
		this.requestBodyRequried = requried;
	}
	
	public void requestResponseInfo(String requestParamName,String responseParamName) {
		this.requestParamName=requestParamName;
		this.responseParamName=responseParamName;
	}

	public void doRequestBody(Action action, MapBean<ActionParamBeanClass> paramBean) throws StrateException {
		try {
			HttpServletRequest req = action.request();
			String contentTypeCharset = req.getHeader(HttpConstans.CONTENT_TYPE);
			String encode = Strate.encoding;
			String contentType;
			if (StringUtils.isEmpty(contentTypeCharset)) {
				contentType = ContentType.getContextType("json");
			} else {
				List<String> contentTypes = CONTENT_TYPE_CHAREST.splitToList(contentTypeCharset);
				contentType=contentTypes.get(0);//第一个为contentType
				if (contentTypes.size() > 1) { //第二个为charset
					String charset = contentTypes.get(1);
					if (Charset.isSupported(charset)) {
						encode = charset;
					} else {
						String upper = charset.toLowerCase(Locale.ENGLISH);
						if (Charset.isSupported(upper)) {
							encode = upper;
						}
					}
				}
			}
			parseRequestBody(req,paramBean,contentType,encode);
		} catch (IOException e) {
			throw new StrateException("read request body error ", e);
		}
	}

	/**
	 * 从requestbody中读取一个字符串
	 * @param req
	 * @param encoding
	 * @return
	 * @throws IOException
	 */
	private String readRequestBodyAsString(HttpServletRequest req, String encoding) throws IOException {
		return IOUtils.readString(new InputStreamReader(req.getInputStream(), encoding));
	}

	/***
	 * 	解析请求体的内容
	 * @param req
	 * @param requestBody
	 * @param paramBean
	 * @throws StrateException
	 * @throws IOException 
	 */
	private void parseRequestBody(HttpServletRequest  req,MapBean<ActionParamBeanClass> paramBean,String contentType,String encoding) throws StrateException, IOException {
		if(StringUtils.endsWith(contentType, "/json")) {
			String requestBody=readRequestBodyAsString(req, encoding);
			parseRequestBodyJson(requestBody, paramBean);
		}else if(StringUtils.endsWith(contentType, "/xml")) {
			String requestBody=readRequestBodyAsString(req, encoding);
			parseRequestBodyXml(requestBody, paramBean);
		}else{//默认使用json格式
			String requestBody=readRequestBodyAsString(req, encoding);
			parseRequestBodyJson(requestBody, paramBean);
		}
	}


	/**
	 * 把请求体当成一个json类型来读取、解析
	 * 
	 * @param requestBody
	 * @param paramBean
	 * @throws StrateException
	 */
	private void parseRequestBodyJson(String requestBody, MapBean<ActionParamBeanClass> paramBean) throws StrateException {
		if (requestBodyRequried) {
			throw new StrateException("request body is requried");
		}
		JSONAware json;
		try {
			json = JSONValue.parse(requestBody);
			ActionParameterFiller.handler.setExpFieldValue(paramBean, requestBodyName, json.value());
		} catch (Exception e) {
			throw new StrateException(e);
		}
	}

	/**
	 * 把请求体当成一个json类型来读取、解析
	 * 
	 * @param requestBody
	 * @param paramBean
	 * @throws StrateException
	 */
	private void parseRequestBodyXml(String requestBody, MapBean<ActionParamBeanClass> paramBean) throws StrateException {
		Map<String, Object> json;
		try {
			json = XmlUtils.parseXmlToMap(requestBody);
			ActionParameterFiller.handler.setExpFieldValue(paramBean, requestBodyName, json);
		} catch (Exception e) {
			throw new StrateException(e);
		}
	}

	public boolean requestBodyRequried() {
		return this.requestBodyRequried;
	}

	public void setRequestParamName(String requestParamName) {
		this.requestParamName = requestParamName;
	}

	public void setResponseParamName(String responseParamName) {
		this.responseParamName = responseParamName;
	}
	/**
	 * 实始化方法参数中以request response类型的参数名称
	 */
	public void initRequestResponseParamName() {
		Class<?>[] paramTypes=getMethod().getParameterTypes();
		List<String> fieldNames=new ArrayList<>(this.properties.keySet());
		for(int i=0;i<paramTypes.length;i++) {
			if(HttpServletRequest.class.isAssignableFrom(paramTypes[i])) {
				this.requestBodyName=fieldNames.get(i);
			}else if(HttpServletResponse.class.isAssignableFrom(paramTypes[i])) {
				this.responseParamName=fieldNames.get(i);
			}
		}
	}
	/**
	 * 	往action方法Bean中注入request 与 response 对象
	 * @param bean
	 * @param req
	 * @param resp
	 * @throws BeanException
	 */
	public void setRequestAndResponse(MapBean<ActionParamBeanClass> bean,HttpServletRequest req,HttpServletResponse resp) throws BeanException {
		if(this.requestParamName!=null) {
			bean.set(this.requestParamName, req);
		}
		if(this.responseParamName!=null) {
			bean.set(this.responseParamName, resp);
		}
	}
	
}
