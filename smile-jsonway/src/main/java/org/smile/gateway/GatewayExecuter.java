package org.smile.gateway;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.smile.Smile;
import org.smile.commons.Strings;
import org.smile.gateway.ServiceAjaxJs.JsCode;
import org.smile.gateway.handler.ServiceHandler;
import org.smile.gateway.invoke.InvokeContext;
import org.smile.gateway.invoke.MethodsGroup;
import org.smile.gateway.json.JsonHandler;
import org.smile.http.HttpFileCache;
import org.smile.http.HttpMethod;
import org.smile.http.RequestAndResponseAware;
import org.smile.io.FileNameUtils;
import org.smile.log.LoggerHandler;

public class GatewayExecuter implements LoggerHandler{
	
	protected static final String filterPath="/resource/";
	
	private static final String RESOURCE_PATH="org/smile/gateway/resource/";
	/***
	 * 文件缓存容器
	 */
	private static final HttpFileCache cacheContext=new HttpFileCache();
	
	protected ServiceHandler serviceHandler;
	
	protected ServletContext servletContext;
	
	protected Map<String,ServiceAjaxJs> serviceAjaxJsMap=new ConcurrentHashMap<String,ServiceAjaxJs>();
	
	protected InvokeContext invkeContext=new InvokeContext();
	
	protected JsonHandler jsonHandler;
	
	protected InvokeFilter invokeFilter;
	
	public GatewayExecuter(ServletContext context){
		servletContext=context;
	}
	
	public Object getServiceBean(String serviceName) throws Exception{
		return serviceHandler.getBean(serviceName, servletContext);
	}

	public void setServiceHandler(ServiceHandler serviceHandler) {
		this.serviceHandler = serviceHandler;
	}

	public void setJsonHandler(JsonHandler jsonHandler) {
		this.jsonHandler = jsonHandler;
	}

	/**
	 * 输出js文件
	 */
	public void writeResourceFile(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String charset = request.getCharacterEncoding();
		if(charset == null){
			charset = Smile.ENCODE;
		}
		String url = request.getRequestURI();
		String outStr =Strings.BLANK;
		int index=url.indexOf(filterPath);//判断是静态文件
		if(index>0){
			logger.debug("do filter "+url);
			String fileName=url.substring(index+filterPath.length());
			String pathName=RESOURCE_PATH+fileName;
			if(cacheContext.isUseCache(request, pathName)){
				HttpFileCache.setHeaderNoModified(response);
			}else{
				byte[] file=cacheContext.findFileByPathName(RESOURCE_PATH+fileName);
				String extName=FileNameUtils.getExtension(fileName);
				HttpFileCache.setHeaderCache(response, extName, 10*1000*60);
				logger.debug("write cache file"+url);
				response.getOutputStream().write(file);
			}
		}else if (url.endsWith(".js")) {//动态生生成的业务方法js
			outStr = generateServiceJavascript(request,response);
			response.setContentType("text/plain;" + charset);	
			response.setStatus(HttpServletResponse.SC_OK);
			write(response, outStr, charset);
		}
	}
	/**
	 * 写入返回内容到response中
	 * @param response
	 * @param content
	 * @param charset
	 * @throws IOException
	 */
	protected void write(HttpServletResponse response,String content,String charset) throws IOException{
		if (content != null) {
			OutputStream out = response.getOutputStream();	
			try{
				byte[] bout = content.getBytes(charset);
				response.setIntHeader("Content-Length", bout.length);
				out.write(bout);
				out.flush();
			}finally{
				out.close();
			}
		}	
	}
	
	protected String getBasePath(HttpServletRequest request){
		String path=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
		return path;
	}
	/**
	 * 创建js文件  
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	public String generateServiceJavascript(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String url = request.getRequestURI();
		if (!url.endsWith(".js")) return Strings.BLANK;
		String serviceName = url.substring(url.lastIndexOf("/")+1,url.lastIndexOf("."));
		String gateway = request.getServletPath();
		String path=getBasePath(request);
		if (!gateway.endsWith("/")) {//处理是否以/结束
			gateway += "/";
		}
		if(gateway.startsWith("/")){
			gateway=gateway.substring(1);
		}
		try {
			ServiceAjaxJs ajaxJs=serviceAjaxJsMap.get(serviceName);
			if(ajaxJs==null){
				ajaxJs=new ServiceAjaxJs(this, serviceName);
				serviceAjaxJsMap.put(path, ajaxJs);
			}
			JsCode code=ajaxJs.getJs(gateway, path);
			if(HttpFileCache.isUseCache(request, code.times)){
				//使用缓存不返回信息
				HttpFileCache.setHeaderNoModified(response);
				return null;
			}else{
				//设置业务js缓存
				HttpFileCache.setHeaderCache(response, "js", 120000);
				return code.content;
			}
		} catch (Exception e) {						
			StringBuffer strbuf = new StringBuffer();			
			strbuf.append("var str=\"generate service error: ").append(serviceName).append("\\n\";");
			strbuf.append("str = str + \"").append(e.getMessage()).append("\";");
			strbuf.append("alert(str);");
			return strbuf.toString();
		}
	}
	/**
	 * 反射服务方法 
	 * @param request
	 * @param response
	 * @return
	 */
	protected String invokeService(HttpServletRequest request, HttpServletResponse response) {		
		String serviceName = request.getParameter("service");
		String methodName = request.getParameter("method");
		String strArgs = request.getParameter("arguments");	
		
		
		String jsonValue = null;
		try {
			Object service = getServiceBean(serviceName);
			if(service instanceof RequestAndResponseAware){
				((RequestAndResponseAware) service).setRequestAndResponse(request, response,HttpMethod.valueOf(request.getMethod()));
			}
			Object result = invokeMethod(request,response,service,methodName,strArgs);
			jsonValue = jsonHandler.toJson(result);
		} catch (Throwable e) {
			logger.error(e);
			Throwable error = e;
			if (e instanceof InvocationTargetException) {
				error = ((InvocationTargetException) e).getTargetException();
			}
			ErrorGatewayResult jsonErrorObject = new ErrorGatewayResult();
			jsonErrorObject.setMessage(error.getMessage());
			jsonErrorObject.setDetails(error.toString());
			jsonErrorObject.setCode(error.getClass().getName());
			try {
				jsonValue = jsonHandler.toJson(jsonErrorObject);
			}catch (Exception me) {
				logger.error(me);
			}
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}		
		return jsonValue;
	}
	/**
	 * 反射方法 
	 * @param service 提供服务的对象
	 * @param methodName 方法名
	 * @param strArgs 方法的参数
	 * @return
	 * @throws NoSuchMethodException
	 * @throws Exception
	 */
	private Object invokeMethod(HttpServletRequest request, HttpServletResponse response,Object service,String methodName,String strArgs) throws NoSuchMethodException,Exception {
		Object[] jsonArgs = jsonHandler.parserJsonToArray(strArgs);
		int argsNum = jsonArgs.length;
		//参数个数一样的方法 
		MethodsGroup methodsGroup = invkeContext.getServiceMethods(methodName,service.getClass()).getMethodsGroup(methodName);
		if(methodsGroup!=null){
			Method targetMethod = null;
			Set<Method> candidateMethods=methodsGroup.getMethodSet(argsNum);
			if(candidateMethods!=null){
				//方法参数
				Object[] args = null;
				//参数类型
				Type[] argTypes=null;
				//循环适配方法
				for (Method method:candidateMethods){
					argTypes= method.getGenericParameterTypes();
					args = new Object[argTypes.length];
					try {
						for (int i=0; i<argTypes.length; i++) {
							args[i] = jsonHandler.toMethodParam(argTypes[i],jsonArgs[i]);	
						}
					} catch (Exception e){
						logger.error("参数类型转换出错",e);
						continue;
					}
					targetMethod = method;
					break;
				}
				//执行方法
				if (targetMethod != null) {
					logger.debug(targetMethod);
					logger.debug(args);
					return invokeFilter.invoke(request, response, service, targetMethod, args);
				}
			}
		}
		//找不到方法抛出展出
		String errStr = "can not find the method [" + methodName + "] in ["+service.getClass()+"], which has " + argsNum + " arguments.";
		throw  new NoSuchMethodException(errStr);
	}
}
