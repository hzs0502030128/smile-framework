package org.smile.gateway;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.smile.Smile;
import org.smile.gateway.handler.ServiceHandler;
import org.smile.gateway.handler.SmileServiceHandler;
import org.smile.gateway.invoke.BaseOpenWayControler;
import org.smile.gateway.invoke.OpenWayControler;
import org.smile.gateway.json.JsonHandler;
import org.smile.gateway.json.SmileJsonHandler;
import org.smile.log.LoggerHandler;
import org.smile.reflect.ClassTypeUtils;
import org.smile.util.StringUtils;

/**
 * json gateway 
 * @author strive
 *
 */
public class GatewayServlet extends HttpServlet implements LoggerHandler {
	/**方法执行*/
	private GatewayExecuter executer;
	/**
	 * get 只处理获取js文件的请求
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		executer.writeResourceFile(request, response);
	}

	/**
	 * post 用于处理业务方法
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String charset = request.getCharacterEncoding();
		if (charset == null){
			charset = Smile.ENCODE;
		}
		response.setContentType("text/plain;" + charset);
		response.setStatus(HttpServletResponse.SC_OK);
		String resultStr = executer.invokeService(request, response);
		if (resultStr != null) {
			OutputStream out = response.getOutputStream();
			try{
				byte[] bout = resultStr.getBytes(charset);
				response.setIntHeader("Content-Length", bout.length);
				out.write(bout);
				out.flush();
			}finally{
				out.close();
			}
		}
	}

	@Override
	public void init() throws ServletException {
		executer = new GatewayExecuter(getServletContext());
		//服务组件获取配置
		String serviceHandlerClass=getInitParameter("ServiceHandler");
		if(serviceHandlerClass!=null){
			try{
				ServiceHandler serviceHandler=(ServiceHandler)Class.forName(serviceHandlerClass).newInstance();
				executer.setServiceHandler(serviceHandler);
			}catch(Exception e){
				throw new ServletException("初始化ServiceHandler配置信息失败",e);
			}
		}else{
			executer.setServiceHandler(new SmileServiceHandler());
		} 
		//json处理配置
		String jsonHandlerClass=getInitParameter("JsonHandler");
		if(jsonHandlerClass!=null){
			try{	
				JsonHandler jsonHandler=(JsonHandler)Class.forName(jsonHandlerClass).newInstance();
				executer.setJsonHandler(jsonHandler);
			}catch(Exception e){
				throw new ServletException("初始化SmileJsonHandler配置信息失败",e);
			}
		}else{
			executer.setJsonHandler(new SmileJsonHandler());
		}
		OpenWayControler openGatewayControler;
		//配置开启入口配置
		String openGatewayControlerClass=getInitParameter("OpenWayControler");
		if(StringUtils.notEmpty(openGatewayControlerClass)){
			try{
				openGatewayControler=(OpenWayControler)ClassTypeUtils.newInstance(openGatewayControlerClass);
			}catch(Exception e){
				throw new ServletException("初始化OpenWayControler配置信息失败",e);
			}
		}else{
			openGatewayControler=new BaseOpenWayControler();
		}
		executer.invkeContext.setControler(openGatewayControler);
		//执行过滤器
		String invokeFilterClass=getInitParameter("InvokeFilter");
		if(invokeFilterClass!=null){
			try{	
				InvokeFilter invokeFilter=(InvokeFilter)ClassTypeUtils.newInstance(invokeFilterClass);
				executer.invokeFilter=invokeFilter;
			}catch(Exception e){
				throw new ServletException("初始化InvokeFilter配置信息失败",e);
			}
		}else{
			executer.invokeFilter=new InvokeFilter();
		}
	}
}
